package com.artformgames.plugin.votepass.api.data.request;

import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
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

    protected Set<VoteInformation> votes;
    protected @Nullable UserKey assignee;

    protected @NotNull RequestResult result;
    protected boolean feedback;

    protected final @NotNull LocalDateTime createTime;
    protected @Nullable LocalDateTime closedTime;

    public RequestInformation(int id, @NotNull String server, @NotNull UserKey user,
                              @NotNull Map<Integer, RequestAnswer> contents,
                              Set<VoteInformation> votes, @Nullable UserKey assignee,
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

    public @Nullable LocalDateTime getEndTime(long autoCloseTime) {
        if (autoCloseTime <= 0) return null;
        return getCreateTime().plus(autoCloseTime, ChronoUnit.SECONDS);
    }

    public @Nullable Long getRemainMills(long autoCloseTime) {
        LocalDateTime endTime = getEndTime(autoCloseTime);
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

    public boolean isTimeout(@Nullable Duration keepTime) {
        if (keepTime == null) return false;
        LocalDateTime finalTime = LocalDateTime.now().plus(keepTime);
        return LocalDateTime.now().isAfter(finalTime);
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

}
