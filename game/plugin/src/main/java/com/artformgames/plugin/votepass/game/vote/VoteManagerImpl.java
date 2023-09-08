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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

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
            Main.severe("Failed to synchronize new request data from the database, please check the database connection configuration!");
            exception.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getAutoPassRatio(int total) {
        Map<Integer, Double> ratioMap = PluginConfig.SERVER.AUTO_PASS_RATIO.getNotNull();
        if (ratioMap.isEmpty()) return 0;

        double ratio = 0;
        for (Map.Entry<Integer, Double> entry : ratioMap.entrySet()) {
            if (ratio == 0 || total > entry.getKey()) {
                ratio = entry.getValue();
            } else {
                return ratio;
            }
        }
        return ratio;
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
    public int countRequest(@Nullable Predicate<RequestInformation> predicate) {
        if (predicate == null) return requests.size();
        else return (int) this.requests.values().stream().filter(predicate).count();
    }

    @Override
    public int countAdminRequests() {
        return countRequest(r -> r.needIntervention(CommonConfig.TIME.ADMIN_INTERVENTION.get()));
    }

    @Override
    public @NotNull CompletableFuture<VoteInformation> submitVote(GameUserData voter, PendingVote pendingVote) {
        RequestInformation request = pendingVote.getRequest();

        VoteInformation vote = new VoteInformation(
                pendingVote.getRequest().getID(), voter.getKey(),
                Optional.ofNullable(pendingVote.getDecision()).orElse(VoteDecision.ABSTAIN),
                pendingVote.getComments(), LocalDateTime.now()
        );

        return DataTables.VOTES.createReplace()
                .setColumnNames("request", "voter", "decision", "comment", "time")
                .setParams(
                        vote.requestID(), vote.voter().id(),
                        vote.decision().getID(), vote.comment(), LocalDateTime.now()
                ).executeFuture(changes -> {
                    request.addVote(vote);
                    return changes;
                }).thenApply(i -> {
                    RequestResult result = calculateResult(request);
                    Main.debugging("Calculated #" + request.getID() + " 's result -> " + result.name());
                    if (result == RequestResult.APPROVED) {
                        approve(request);
                    } else if (result == RequestResult.REJECTED) {
                        reject(request);
                    }
                    return i;
                }).thenApply(i -> vote);
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

        int total = countable + missed - abs;
        double ratio = getAutoPassRatio(total);
        if (ratio <= 0) {
            Main.debugging("Players | size: " + total + " / voted: " + (pros + cons) + " (Auto pass disabled).");
            return RequestResult.PENDING; // Disabled auto pass.
        }

        int autoApprove = (int) (total * ratio);
        int autoDeny = total - autoApprove;

        Main.debugging("Players | size: " + total + " / voted: " + (pros + cons));
        Main.debugging("Auto approve required " + (ratio * 100) + "%  (Now " + pros + "/" + autoApprove + ")");
        Main.debugging("Auto reject required " + ((1 - ratio) * 100) + "% (Now " + cons + "/" + autoDeny + ")");

        if (pros >= autoApprove) return RequestResult.APPROVED;
        else if (cons >= autoDeny) return RequestResult.REJECTED;
        else return RequestResult.PENDING;
    }

    @Override
    public CompletableFuture<Boolean> approve(@NotNull RequestInformation request) {
        return CompletableFuture.supplyAsync(() -> {
            // Add the player to the whitelist
            try {
                Main.getInstance().getUserManager().modifyWhitelist().add(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }).thenCompose(result -> updateResult(request, RequestResult.APPROVED));
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

}
