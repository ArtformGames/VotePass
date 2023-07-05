package com.artformgames.plugin.votepass.lobby.user;

import com.artformgames.plugin.votepass.api.data.request.RequestInfo;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.user.AbstractUserData;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LobbyUser extends AbstractUserData implements LobbyUserData {

    private final @NotNull Map<Integer, RequestInfo> requests;
    private final @NotNull Set<String> passedServers;

    private @Nullable PendingRequest pendingRequest;

    protected LobbyUser(@NotNull UserKey key,
                        @NotNull Map<Integer, RequestInfo> requests,
                        @NotNull Set<String> passedServers) {
        super(key);
        this.requests = requests;
        this.passedServers = passedServers;
    }

    @Override
    public @Unmodifiable @NotNull Map<Integer, RequestInfo> listRequests() {
        return Collections.unmodifiableMap(this.requests);
    }

    @Override
    public @Unmodifiable @NotNull Set<String> listPassedServers() {
        return Collections.unmodifiableSet(this.passedServers);
    }

    @Override
    public @Nullable PendingRequest getPendingRequest() {
        return this.pendingRequest;
    }

    @Override
    public void removePendingRequest() {
        this.pendingRequest = null;
    }

    @Override
    public @NotNull PendingRequest createPendingRequest(@NotNull ServerSettings settings) {
        return pendingRequest = new PendingRequest(settings);
    }

    public void addRequest(@NotNull RequestInfo request) {
        this.requests.put(request.getID(), request);
    }

    public void removeRequest(int requestID) {
        this.requests.remove(requestID);
    }

    @Override
    public @Nullable RequestInfo getRequest(int requestID) {
        return this.requests.get(requestID);
    }

    @Override
    public @Nullable RequestInfo getServerRequest(String serverID) {
        return this.requests.values().stream().filter(request -> request.getServer().equals(serverID)).findFirst().orElse(null);
    }



}
