package com.artformgames.plugin.votepass.lobby.api.user;

import com.artformgames.plugin.votepass.api.data.request.RequestContent;
import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.Set;

public interface LobbyUserData extends UserData {

    @Unmodifiable
    @NotNull Map<Integer, RequestContent> listRequests();

    @Unmodifiable
    @NotNull Set<String> listPassedServers();

    default boolean isPassed(@NotNull String serverID) {
        return listPassedServers().contains(serverID);
    }

    PendingRequest getPendingRequest();

    void removePendingRequest();

    void createPendingRequest(@NotNull String serverID);

    default boolean hasRequested(@NotNull String serverID) {
        return listRequests().values().stream().anyMatch(requestContent -> requestContent.getServer().equals(serverID));
    }

}
