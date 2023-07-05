package com.artformgames.plugin.votepass.lobby.api.server;

import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public interface ServerSettingsManager {

    void reloadSettings();

    @Unmodifiable
    @NotNull Set<ServerSettings> list();

    @Nullable ServerSettings getSettings(@NotNull String serverID);

    boolean isDisabled(@NotNull String serverID);

    void setDisabled(@NotNull String serverID, boolean disabled);

}
