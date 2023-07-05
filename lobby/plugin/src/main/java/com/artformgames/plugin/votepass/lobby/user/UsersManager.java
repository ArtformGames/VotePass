package com.artformgames.plugin.votepass.lobby.user;

import cc.carm.lib.easyplugin.EasyPlugin;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;

public class UsersManager extends AbstractUserManager<LobbyUser> implements LobbyUserManager<LobbyUser> {

    protected UsersManager(@NotNull EasyPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull LobbyUser empty(@NotNull UserKey key) {
        return new LobbyUser(key, new HashMap<>(), new HashSet<>());
    }

    @Override
    protected @Nullable LobbyUser loadData(@NotNull UserKey key) throws Exception {


        return null;
    }

    @Override
    protected void saveData(@NotNull LobbyUser data) throws Exception {

    }

}
