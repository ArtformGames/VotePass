package com.artformgames.plugin.votepass.game.user;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.user.AbstractUserData;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.VotePassServerAPI;
import com.artformgames.plugin.votepass.game.api.user.GameUserData;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameUser extends AbstractUserData implements GameUserData {

    private @Nullable PendingVote pendingVote;

    public GameUser(@NotNull UserKey key) {
        super(key);
    }

    @Override
    public @NotNull @Unmodifiable Map<Integer, VoteInformation> listVotes() {
        return Main.getInstance().getVoteManager().getRequests().values().stream()
                .map(value -> value.getVote(key)).filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableMap(VoteInformation::requestID, v -> v, (a, b) -> b));
    }

    @Override
    public int countUnvotedRequest() {
        return (int) VotePassServerAPI.getVoteManager().getRequests().values()
                .stream().filter(value -> !value.isVoted(key)).count();
    }

    @Override
    public boolean isVoted(int requestID) {
        RequestInformation info = VotePassServerAPI.getVoteManager().getRequest(requestID);
        return info != null && info.isVoted(key);
    }

    @Override
    public @Nullable PendingVote getPendingVote() {
        return this.pendingVote;
    }

    @Override
    public @NotNull PendingVote createPendingRequest(@NotNull RequestInformation request) {
        this.pendingVote = new PendingVote(request);
        return this.pendingVote;
    }

    @Override
    public void removePendingVote() {
        this.pendingVote = null;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
