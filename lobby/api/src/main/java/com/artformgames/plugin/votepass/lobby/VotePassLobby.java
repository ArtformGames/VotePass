package com.artformgames.plugin.votepass.lobby;

import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import com.artformgames.plugin.votepass.lobby.api.server.ServerSettingsManager;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import org.jetbrains.annotations.NotNull;

interface VotePassLobby {

    @NotNull LobbyUserManager<? extends LobbyUserData> getUserManager();

    @NotNull ServerSettingsManager getServersManager();

    @NotNull UserRequestManager getRequestManager();

}
