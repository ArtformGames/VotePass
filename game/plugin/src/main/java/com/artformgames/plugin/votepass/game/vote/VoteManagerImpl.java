package com.artformgames.plugin.votepass.game.vote;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.core.database.DataTables;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.user.GameUserData;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.api.vote.VoteManager;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class VoteManagerImpl implements VoteManager {

    private final @NotNull SortedMap<Integer, RequestInformation> requests = new TreeMap<>();

    @Override
    public int sync() {
        try {
            int startID = getLastKey();
            Duration closeDuration = CommonConfig.TIME.AUTO_CLOSE.get();

            Map<Integer, RequestInformation> data = Main.getInstance().getDataManager()
                    .queryRequests(builder -> {
                        if (startID > 0) builder.addCondition("id", ">", startID);
                        builder.addCondition("server", Main.getInstance().getServerID());
                        builder.addCondition("result", 0);
                        if (closeDuration != null) {
                            long end = System.currentTimeMillis();
                            long start = end - closeDuration.toMillis();
                            builder.addTimeCondition("create_time", start, end);
                        }
                    });

            if (!data.isEmpty()) this.requests.putAll(data);

            return data.size();
        } catch (Exception exception) {
            Main.severe("从服务器同步新请求数据失败，请检查数据库连接配置！");
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getAutoPassRatio() {
        return PluginConfig.SERVER.PASS_RATIO.getNotNull();
    }

    @Override
    public @NotNull SortedMap<Integer, RequestInformation> getRequests() {
        return Collections.unmodifiableSortedMap(this.requests);
    }

    @Override
    public @Nullable RequestInformation getRequest(int requestID) {
        return this.requests.get(requestID);
    }

    @Override
    public @NotNull VoteInformation submitVote(GameUserData voter, PendingVote pendingVote) {
        RequestInformation request = pendingVote.getRequest();

        VoteInformation vote = new VoteInformation(
                pendingVote.getRequest().getID(), voter.getKey(),
                Optional.ofNullable(pendingVote.getDecision()).orElse(VoteDecision.ABSTAIN),
                pendingVote.getComments(), LocalDateTime.now()
        );

        DataTables.VOTES.createReplace()
                .setColumnNames("request", "voter", "decision", "comment", "time")
                .setParams(
                        vote.requestID(), vote.voter().id(),
                        vote.decision().getID(), vote.comment(), LocalDateTime.now()
                ).returnGeneratedKey().executeAsync();

        request.addVote(vote);

        RequestResult result = calculateResult(request);
        Main.debugging("Calculated #" + request.getID() + " 's result -> " + result.name());
        if (result == RequestResult.APPROVED) {
            approve(request);
        } else if (result == RequestResult.REJECTED) {
            reject(request);
        }

        return vote;
    }

    @Override
    public @NotNull RequestResult calculateResult(@NotNull RequestInformation request) {
        UsersManager manager = Main.getInstance().getUserManager();

        int countable = manager.countUser(WhitelistedUserData::isVoteCountable);   // General counting.

        int pros = 0;
        int abs = 0;
        int cons = 0;
        int missed = 0;

        for (VoteInformation vote : request.getVotes()) {
            switch (vote.decision()) {
                case ABSTAIN -> abs++;
                case APPROVE -> pros++;
                case REJECT -> cons++;
            }
            WhitelistedUserData data = manager.getWhitelistData(vote.voter());
            if (data != null && !data.isVoteCountable()) {
                missed++; // Data is uncountable but exists in votes, missed in general counting.
            }
        }

        double base = countable + missed - abs; // Base num means All countable
        double ratio = getAutoPassRatio();

        double autoApprove = base * ratio;
        double autoDeny = base - autoApprove;

        Main.debugging("Players | total: " + base + " / voted: " + (pros + cons));
        Main.debugging("Auto approve required " + (ratio * 100) + "%  (Now " + pros + "/" + autoApprove + ")");
        Main.debugging("Auto reject required " + ((1 - ratio) * 100) + "% (Now " + cons + "/" + autoDeny + ")");


        if (pros >= autoApprove) return RequestResult.APPROVED;
        else if (cons >= autoDeny) return RequestResult.REJECTED;
        else return RequestResult.PENDING;
    }

    @Override
    public CompletableFuture<Boolean> approve(@NotNull RequestInformation request) {
        return Main.getInstance().getUserManager().modifyWhitelist() // Add the player to the whitelist
                .add(request).execute().thenCompose(changes -> updateResult(request, RequestResult.APPROVED));
    }

    @Override
    public CompletableFuture<Boolean> reject(@NotNull RequestInformation request) {
        return updateResult(request, RequestResult.REJECTED);
    }

    @Override
    public CompletableFuture<Boolean> updateResult(@NotNull RequestInformation request, @NotNull RequestResult result) {
        this.requests.remove(request.getID());         // Remove from cache

        request.setResult(result);
        request.setCloseTime(LocalDateTime.now());

        return Main.getInstance().getDataManager().updateRequest(request);
    }

    public int getLastKey() {
        return requests.isEmpty() ? 0 : requests.lastKey();
    }

    public Map<Integer, RequestInformation> getServerActiveRequests(@NotNull String serverID, int startID,
                                                                    long startLimit, long endLimit) throws SQLException {
        return Main.getInstance().getDataManager().queryRequests(builder -> {
            if (startID > 0) builder.addCondition("id", ">", startID);
            builder.addCondition("server", serverID);
            builder.addCondition("result", 0);
            builder.addTimeCondition("create_time", startLimit, endLimit);
        });
    }

}
