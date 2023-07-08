package com.artformgames.plugin.votepass.lobby.user;

import cc.carm.lib.easyplugin.EasyPlugin;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.database.DataManager;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UsersManager extends AbstractUserManager<LobbyUser> implements LobbyUserManager<LobbyUser> {

    public UsersManager(@NotNull EasyPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull LobbyUser empty(@NotNull UserKey key) {
        return new LobbyUser(key, new HashMap<>(), new HashSet<>());
    }

    @Override
    protected @Nullable LobbyUser loadData(@NotNull UserKey key) throws Exception {
        DataManager db = Main.getInstance().getDataManager();

        Set<String> passed = db.getUserPassedServers(key.id());
        Map<Integer, RequestInformation> requests = db.getUserRequests(key.id());

        return new LobbyUser(key, requests, passed);
    }

    @Override
    protected void saveData(@NotNull LobbyUser data) {
        // Lobby data is not saved to the database
    }

}
