package com.artformgames.plugin.votepass.api.data.vote;

import com.artformgames.plugin.votepass.api.user.UserKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public record VoteContent(
        int requestID, @NotNull UserKey voter, @NotNull VoteDecision decision,
        @Nullable String comment, @NotNull LocalDateTime time
) {

    public boolean hasComments() {
        return comment != null && !comment.isBlank();
    }

    public boolean isApproved() {
        return decision == VoteDecision.APPROVE;
    }


}
