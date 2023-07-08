package com.artformgames.plugin.votepass.game;

import com.artformgames.plugin.votepass.game.api.user.GameUserData;
import com.artformgames.plugin.votepass.game.api.user.GameUserManager;
import com.artformgames.plugin.votepass.game.api.vote.VoteManager;
import org.jetbrains.annotations.NotNull;

interface VotePassServer {

    @NotNull String getServerID();

    @NotNull GameUserManager<? extends GameUserData> getUserManager();

    @NotNull VoteManager getVoteManager();

}
