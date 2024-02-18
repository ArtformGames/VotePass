package com.artformgames.plugin.votepass.game.api.event;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class RequestResultUpdatedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    protected final RequestInformation request;

    public RequestResultUpdatedEvent(RequestInformation request) {
        super(true);
        this.request = request;
    }

    public RequestInformation getRequest() {
        return request;
    }

    public RequestResult getResult() {
        return getRequest().getResult();
    }

    public LocalDateTime getTime() {
        return getRequest().getCloseTime();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }


}
