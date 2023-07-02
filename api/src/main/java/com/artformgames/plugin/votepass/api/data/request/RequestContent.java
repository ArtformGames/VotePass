package com.artformgames.plugin.votepass.api.data.request;

import com.artformgames.plugin.votepass.api.data.vote.VoteContent;
import com.artformgames.plugin.votepass.api.user.UserKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RequestContent {

    private final int id;

    protected final @NotNull String server;
    protected final @NotNull UserKey user;

    protected final @NotNull Map<Integer, RequestAnswer> contents;

    protected Set<VoteContent> votes;
    protected @Nullable UUID assignee;

    protected @NotNull RequestResult result;
    protected boolean feedback;

    protected final @NotNull LocalDateTime createTime;
    protected @Nullable LocalDateTime closedTime;

    public RequestContent(int id, @NotNull String server, @NotNull UserKey user,
                          @NotNull Map<Integer, RequestAnswer> contents,
                          Set<VoteContent> votes, @Nullable UUID assignee,
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

    public String getServer() {
        return server;
    }



}
