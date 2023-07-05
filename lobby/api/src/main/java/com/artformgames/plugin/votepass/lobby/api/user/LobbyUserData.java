package com.artformgames.plugin.votepass.lobby.api.user;

import com.artformgames.plugin.votepass.api.data.request.RequestInfo;
import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;

public interface LobbyUserData extends UserData {

    @Unmodifiable
    @NotNull Map<Integer, RequestInfo> listRequests();

    @Unmodifiable
    @NotNull Set<String> listPassedServers();

    default boolean isPassed(@NotNull String serverID) {
        return listPassedServers().contains(serverID);
    }

    @Nullable PendingRequest getPendingRequest();

    void removePendingRequest();

    @NotNull PendingRequest createPendingRequest(@NotNull ServerSettings settings);

    void addRequest(@NotNull RequestInfo content);

    void removeRequest(int id);

    @Nullable RequestInfo getRequest(int requestID);

    @Nullable RequestInfo getServerRequest(String serverID);

    default boolean hasRequested(@NotNull String serverID) {
        return listRequests().values().stream().anyMatch(requestContent -> requestContent.getServer().equals(serverID));
    }

}
