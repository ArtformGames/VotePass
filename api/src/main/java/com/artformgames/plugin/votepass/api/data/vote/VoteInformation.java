package com.artformgames.plugin.votepass.api.data.vote;

import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.api.utils.TimeFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record VoteInformation(
        int requestID, @NotNull UserKey voter, @NotNull VoteDecision decision,
        @Nullable String comment, @NotNull LocalDateTime time
) {

    public boolean hasComments() {
        return comment != null && !comment.isBlank();
    }

    public boolean isApproved() {
        return decision == VoteDecision.APPROVE;
    }

    public @NotNull String getTimeString() {
        return TimeFormatUtils.formatTime(time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VoteInformation that = (VoteInformation) o;

        if (requestID != that.requestID) return false;
        return voter.equals(that.voter);
    }

    @Override
    public int hashCode() {
        int result = requestID;
        result = 31 * result + voter.hashCode();
        return result;
    }

}
