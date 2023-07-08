package com.artformgames.plugin.votepass.game.api.vote;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import org.jetbrains.annotations.Nullable;

public class PendingVote {

    private final RequestInformation request;

    @Nullable VoteDecision decision;
    @Nullable String comments;

    public PendingVote(RequestInformation request) {
        this.request = request;
    }

    public RequestInformation getRequest() {
        return request;
    }

    public @Nullable VoteDecision getDecision() {
        return decision;
    }

    public boolean isApproved() {
        return this.decision == VoteDecision.APPROVE;
    }

    public @Nullable String getComments() {
        return comments;
    }

    public void setDecision(@Nullable VoteDecision decision) {
        this.decision = decision;
    }

    public void setComments(@Nullable String comments) {
        this.comments = comments;
    }

}
