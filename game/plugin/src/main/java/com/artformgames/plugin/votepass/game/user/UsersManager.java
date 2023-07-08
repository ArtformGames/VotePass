package com.artformgames.plugin.votepass.game.user;

import cc.carm.lib.easyplugin.EasyPlugin;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.game.api.user.GameUserManager;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistModifier;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.migrator.WhitelistModifierImpl;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class UsersManager extends AbstractUserManager<GameUser> implements GameUserManager<GameUser> {

    protected final Map<UUID, WhitelistUser> whitelistMap = new HashMap<>();

    protected UsersManager(@NotNull EasyPlugin plugin) {
        super(plugin);
    }

    @Override
    public @NotNull GameUser empty(@NotNull UserKey key) {
        return new GameUser(key);
    }

    @Override
    protected @Nullable GameUser loadData(@NotNull UserKey key) {
        return new GameUser(key);
    }

    @Override
    protected void saveData(@NotNull GameUser data) throws Exception {
        WhitelistedUserData user = getWhitelistData(data.getUserUUID());
        if (user == null) return;

        user.updateLastOnline();
        modifyWhitelist().update(user).execute().get();
    }

    @Override
    public @NotNull WhitelistModifier modifyWhitelist() {
        return new WhitelistModifierImpl();
    }

    @Override
    public int countUser(Predicate<WhitelistedUserData> filter) {
        return Math.toIntExact(this.whitelistMap.values().stream().filter(filter).count());
    }

    @Override
    public @NotNull Set<WhitelistedUserData> getWhitelists() {
        return ImmutableSet.copyOf(this.whitelistMap.values());
    }

    @Override
    public @Nullable WhitelistedUserData getWhitelistData(UUID user) {
        return this.whitelistMap.get(user);
    }

    @Override
    public boolean isWhitelisted(@NotNull UUID uuid) {
        return this.whitelistMap.containsKey(uuid);
    }

    public void addWhitelist(WhitelistUser user) {
        this.whitelistMap.put(user.getUserUUID(), user);
    }

    public boolean removeWhitelist(UserKey key) {
        return this.whitelistMap.remove(key.uuid()) != null;
    }

}
