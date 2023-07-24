package com.artformgames.plugin.votepass.game.user;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easysql.api.SQLQuery;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.database.DataTables;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.user.GameUserManager;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistModifier;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.whitelist.WhitelistModifierImpl;
import com.artformgames.plugin.votepass.game.whitelist.WhitelistUser;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.Predicate;

public class UsersManager extends AbstractUserManager<GameUser> implements GameUserManager<GameUser> {

    protected final Map<UUID, WhitelistUser> whitelistMap = new HashMap<>();

    public UsersManager(@NotNull EasyPlugin plugin) {
        super(plugin);
    }

    public int loadWhitelist() {
        Map<UUID, WhitelistUser> data = new HashMap<>();
        try (SQLQuery query = DataTables.LIST.createQuery()
                .addCondition("server", Main.getInstance().getServerID())
                .build().execute()) {
            ResultSet rs = query.getResultSet();
            while (rs.next()) {
                UserKey key = getKey(rs.getLong("user"));
                if (key == null) continue;
                WhitelistUser user = new WhitelistUser(
                        key, rs.getInt("request"), rs.getBoolean("abstain"),
                        rs.getTimestamp("passed_time").toLocalDateTime(),
                        Optional.ofNullable(rs.getTimestamp("online_time"))
                                .map(Timestamp::toLocalDateTime).orElse(null)
                );
                data.put(key.uuid(), user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.whitelistMap.clear();
        this.whitelistMap.putAll(data);
        return data.size();
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
        modifyWhitelist().update(user).execute();
    }

    @Override
    public @NotNull WhitelistModifier modifyWhitelist() {
        return new WhitelistModifierImpl();
    }

    @Override
    public int countUser(Predicate<WhitelistedUserData> filter) {
        if (filter == null) return this.whitelistMap.size();
        else return Math.toIntExact(this.whitelistMap.values().stream().filter(filter).count());
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
    public @Nullable WhitelistedUserData getWhitelistData(String user) {
        return this.whitelistMap.values().stream().filter(u -> user.equalsIgnoreCase(u.getKey().name())).findFirst().orElse(null);
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
