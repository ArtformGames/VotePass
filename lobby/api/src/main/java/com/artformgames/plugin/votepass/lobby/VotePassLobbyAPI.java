package com.artformgames.plugin.votepass.lobby;

import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import com.artformgames.plugin.votepass.lobby.api.server.ServerSettingsManager;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import org.jetbrains.annotations.NotNull;

public class VotePassLobbyAPI {

    private VotePassLobbyAPI() {
    }

    protected static VotePassLobby instance = null;

    public static VotePassLobby getInstance() {
        return instance;
    }

    public static @NotNull LobbyUserManager<? extends LobbyUserData> getUserManager() {
        return getInstance().getUserManager();
    }

    public static @NotNull UserRequestManager getRequestManager() {
        return getInstance().getRequestManager();
    }


    public static @NotNull ServerSettingsManager getServersManager() {
        return getInstance().getServersManager();
    }

}
