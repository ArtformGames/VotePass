package com.artformgames.plugin.votepass.server;

import com.artformgames.plugin.votepass.server.api.user.GameUsersManager;
import com.artformgames.plugin.votepass.server.api.vote.VoteManager;
import org.jetbrains.annotations.NotNull;

public class VotePassServerAPI {

    private VotePassServerAPI() {
    }

    protected static VotePassServer instance = null;

    public static VotePassServer getInstance() {
        return instance;
    }

    public static @NotNull GameUsersManager getUsersManager() {
        return getInstance().getUsersManager();
    }

    public static @NotNull VoteManager getVoteManager() {
        return getInstance().getVoteManager();
    }


}
