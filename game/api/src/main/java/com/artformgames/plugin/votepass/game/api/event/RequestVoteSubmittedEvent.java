package com.artformgames.plugin.votepass.game.api.event;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class RequestVoteSubmittedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    protected final RequestInformation request;
    protected final VoteInformation vote;

    public RequestVoteSubmittedEvent(RequestInformation request, VoteInformation vote) {
        super(true);
        this.request = request;
        this.vote = vote;
    }

    public RequestInformation getRequest() {
        return request;
    }

    public VoteInformation getVote() {
        return vote;
    }

    public VoteDecision getDecision() {
        return getVote().decision();
    }

    public UserKey getVoter() {
        return getVote().voter();
    }

    public LocalDateTime getTime() {
        return getVote().time();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }


}