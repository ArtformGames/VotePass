package com.artformgames.plugin.votepass.lobby.api.server;

import com.artformgames.plugin.votepass.lobby.api.data.server.ServerApplication;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface LobbyServersManager {

    void reloadApplications();

    @Unmodifiable
    @NotNull Set<ServerApplication> listApplications();

    @Nullable ServerApplication getApplication(@NotNull String serverID);

    boolean isDisabled(@NotNull String serverID);

    void setDisabled(@NotNull String serverID, boolean disabled);

}
