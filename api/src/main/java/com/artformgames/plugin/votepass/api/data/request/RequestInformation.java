package com.artformgames.plugin.votepass.api.data.request;

import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.api.utils.TimeFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RequestInformation {

    private final int id;

    protected final @NotNull String server;
    protected final @NotNull UserKey user;

    protected final @NotNull Map<Integer, RequestAnswer> contents;

    protected @NotNull Set<VoteInformation> votes;
    protected @Nullable UserKey assignee;

    protected @NotNull RequestResult result;
    protected boolean feedback;

    protected final @NotNull LocalDateTime createTime;
    protected @Nullable LocalDateTime closedTime;

    public RequestInformation(int id, @NotNull String server, @NotNull UserKey user,
                              @NotNull Map<Integer, RequestAnswer> contents,
                              @NotNull Set<VoteInformation> votes, @Nullable UserKey assignee,
                              @NotNull RequestResult result, boolean feedback,
                              @NotNull LocalDateTime createTime, @Nullable LocalDateTime closedTime) {
        this.id = id;
        this.server = server;
        this.user = user;
        this.contents = contents;
        this.votes = votes;
        this.assignee = assignee;
        this.result = result;
        this.feedback = feedback;
        this.createTime = createTime;
        this.closedTime = closedTime;
    }

    public int getID() {
        return id;
    }

    public @NotNull String getServer() {
        return server;
    }

    public @NotNull UserKey getUser() {
        return user;
    }

    public @NotNull UUID getUserUUID() {
        return getUser().uuid();
    }

    public @Nullable String getUsername() {
        return getUser().name();
    }

    public @NotNull String getUserDisplayName() {
        return getUser().getDisplayName();
    }

    public @NotNull Map<Integer, RequestAnswer> getContents() {
        return contents;
    }

    public @NotNull Set<VoteInformation> getVotes() {
        return votes;
    }

    public int countCommentedVotes() {
        return Math.toIntExact(getVotes().stream().filter(VoteInformation::hasComments).count());
    }

    public void addVote(@NotNull VoteInformation vote) {
        this.votes.add(vote);
    }

    public @Nullable UserKey getAssignee() {
        return assignee;
    }

    public @NotNull RequestResult getResult() {
        return result;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public @NotNull LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public @NotNull String getCreateTimeString() {
        return TimeFormatUtils.formatTime(getCreateTime());
    }

    public @Nullable LocalDateTime getExpireTime(@Nullable Duration expireDuration) {
        if (expireDuration == null) return null;
        return getCreateTime().plus(expireDuration);
    }

    public @NotNull String getExpireTimeString(@Nullable Duration expireDuration) {
        LocalDateTime end = getExpireTime(expireDuration);
        if (end == null) return "∞";
        return TimeFormatUtils.formatTime(end);
    }

    public @Nullable Long getRemainMills(@Nullable Duration expireDuration) {
        LocalDateTime endTime = getExpireTime(expireDuration);
        if (endTime == null) return null;
        return LocalDateTime.now().until(endTime, ChronoUnit.MILLIS);
    }

    public @Nullable LocalDateTime getCloseTime() {
        return this.closedTime;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    public int count(@Nullable VoteDecision decision) {
        if (decision == null) return getVotes().size();
        else return Math.toIntExact(votes.stream().filter(vote -> vote.decision() == decision).count());
    }

    public int countAnswerWords() {
        return getContents().values().stream().mapToInt(RequestAnswer::countWords).sum();
    }

    public void setAssignee(@Nullable UserKey assignee) {
        this.assignee = assignee;
    }

    public void setResult(@NotNull RequestResult result) {
        this.result = result;
    }

    public void setCloseTime(@Nullable LocalDateTime closeTime) {
        this.closedTime = closeTime;
    }

    public boolean isTimeout(@Nullable Duration expireDuration) {
        LocalDateTime finalTime = getExpireTime(expireDuration);
        if (finalTime == null) return false;
        else return LocalDateTime.now().isAfter(finalTime);
    }

    public boolean needIntervention(@Nullable Duration interventionTime) {
        if (interventionTime == null) return false;
        LocalDateTime handleTime = LocalDateTime.now().plus(interventionTime);
        return LocalDateTime.now().isAfter(handleTime);
    }

    public @Nullable VoteInformation getVote(UserKey key) {
        return getVotes().stream().filter(vote -> vote.voter().equals(key)).findFirst().orElse(null);
    }

    public @Nullable VoteInformation getVote(UUID uuid) {
        return getVotes().stream().filter(vote -> vote.voter().uuid().equals(uuid)).findFirst().orElse(null);
    }

    public void removeVote(UUID uuid) {
        getVotes().removeIf(vote -> vote.voter().uuid().equals(uuid));
    }

    public boolean isVoted(UUID uuid) {
        return getVotes().stream().anyMatch(vote -> vote.voter().uuid().equals(uuid));
    }

    public boolean isVoted(UserKey key) {
        return getVotes().stream().anyMatch(vote -> vote.voter().equals(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestInformation that = (RequestInformation) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
