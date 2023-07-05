package com.artformgames.plugin.votepass.server;

import com.artformgames.plugin.votepass.server.api.user.GameUsersManager;
import com.artformgames.plugin.votepass.server.api.vote.VoteManager;
import org.jetbrains.annotations.NotNull;

interface VotePassServer {


    @NotNull GameUsersManager getUsersManager();

    @NotNull VoteManager getVoteManager();

}
