package com.artformgames.plugin.votepass.lobby;

import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import com.artformgames.plugin.votepass.lobby.api.server.LobbyServersManager;
import org.jetbrains.annotations.NotNull;

interface VotePassLobby {


    @NotNull LobbyUserManager getUserManager();

    @NotNull LobbyServersManager getServersManager();

}
