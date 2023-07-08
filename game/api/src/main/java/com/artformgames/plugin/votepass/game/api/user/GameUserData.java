package com.artformgames.plugin.votepass.game.api.user;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.game.VotePassServerAPI;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface GameUserData extends UserData {

    default @Nullable WhitelistedUserData getWhitelistData() {
        return VotePassServerAPI.getUserManager().getWhitelistData(getUserUUID());
    }

    @NotNull
    @Unmodifiable Map<Integer, VoteInformation> listVotes();

    @NotNull
    default @Unmodifiable SortedMap<Integer, RequestInformation> getUnhandledRequests() {
        return VotePassServerAPI.getVoteManager().getRequests().entrySet().stream()
                .filter(entry -> !isVoted(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, TreeMap::new));
    }

    int countUnhandledRequest();

    boolean isVoted(int requestID);

    @Nullable PendingVote getPendingVote();

    @NotNull PendingVote createPendingRequest(@NotNull RequestInformation request);

    void removePendingVote();


}
