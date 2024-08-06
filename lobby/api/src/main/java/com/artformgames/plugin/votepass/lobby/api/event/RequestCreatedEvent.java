package com.artformgames.plugin.votepass.lobby.api.event;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RequestCreatedEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    protected final RequestInformation request;

    public RequestCreatedEvent(RequestInformation request) {
        super(true);
        this.request = request;
    }

    public RequestInformation getRequest() {
        return request;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }


}
