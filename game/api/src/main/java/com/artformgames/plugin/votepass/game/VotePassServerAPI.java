package com.artformgames.plugin.votepass.game;

import com.artformgames.plugin.votepass.game.api.user.GameUserData;
import com.artformgames.plugin.votepass.game.api.user.GameUserManager;
import com.artformgames.plugin.votepass.game.api.vote.VoteManager;
import org.jetbrains.annotations.NotNull;

public class VotePassServerAPI {

    private VotePassServerAPI() {
    }

    protected static VotePassServer instance = null;

    public static VotePassServer getInstance() {
        return instance;
    }

    public static @NotNull String getServerID() {
        return getInstance().getServerID();
    }

    public static @NotNull GameUserManager<? extends GameUserData> getUserManager() {
        return getInstance().getUserManager();
    }

    public static @NotNull VoteManager getVoteManager() {
        return getInstance().getVoteManager();
    }


}
