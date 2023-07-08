package com.artformgames.plugin.votepass.server.api.user;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInfomation;
import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.server.VotePassServerAPI;
import com.artformgames.plugin.votepass.server.api.vote.PendingVote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface VoteUserData extends UserData {

    @NotNull
    @Unmodifiable Map<Integer, VoteInfomation> listVotes();

    @NotNull
    default @Unmodifiable SortedMap<Integer, RequestInformation> getUnhandledRequests() {
        return VotePassServerAPI.getVoteManager().getRequests().entrySet().stream()
                .filter(entry -> !isVoted(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, TreeMap::new));
    }

    int countUnhandledRequest();

    void addVote(@NotNull VoteInfomation vote);

    void removeVote(int requestID);

    default void removeVote(@NotNull RequestInformation request) {
        removeVote(request.getID());
    }

    boolean isVoted(int requestID);

    @Nullable PendingVote getPendingVote();

    @NotNull PendingVote createPendingRequest(@NotNull RequestInformation request);

    void removePendingVote();


}
