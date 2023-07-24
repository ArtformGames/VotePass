package com.artformgames.plugin.votepass.game.api.vote;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PendingVote {

    private final @NotNull RequestInformation request;

    @Nullable VoteDecision decision;
    @Nullable String comments;

    public PendingVote(@NotNull RequestInformation request) {
        this.request = request;
    }

    public @NotNull RequestInformation getRequest() {
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

    public void setComments(@Nullable List<String> comments) {
        if (comments == null || comments.isEmpty()) setComments((String) null);
        else setComments(String.join(", ", comments.stream().filter(s -> s.length() > 0).toList()));
    }


}
