package com.artformgames.plugin.votepass.game.api.user;

import com.artformgames.plugin.votepass.api.user.UserDataManager;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistModifier;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface GameUserManager<U extends GameUserData> extends UserDataManager<U> {

    /**
     * Create a migrator which could users and operators to database
     * from other sources like 'whitelist.json'.
     *
     * @return The future of the number of users imported.
     */
    @NotNull WhitelistModifier modifyWhitelist();

    int countUser(@Nullable Predicate<WhitelistedUserData> filter);

    @NotNull
    @Unmodifiable Set<WhitelistedUserData> getWhitelists();

    @Nullable WhitelistedUserData getWhitelistData(UUID user);

    @Nullable WhitelistedUserData getWhitelistData(String user);

    default @Nullable WhitelistedUserData getWhitelistData(UserKey user) {
        return getWhitelistData(user.uuid());
    }


    boolean isWhitelisted(@NotNull UUID uuid);

    default boolean isWhitelisted(@NotNull UserKey user) {
        return isWhitelisted(user.uuid());
    }

    default boolean isAdmin(Player player) {
        return player.isOnline() || player.hasPermission("VotePass.admin");
    }

}
