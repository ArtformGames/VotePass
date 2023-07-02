package com.artformgames.plugin.votepass.server.api.user;

import com.artformgames.plugin.votepass.api.user.UserDataManager;
import com.artformgames.plugin.votepass.api.user.UserKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ServerUserManager extends UserDataManager<ServerUserData> {

    /**
     * Import users and operators to database from other sources like 'whitelist.json'.
     *
     * @param users The users to import.
     * @return The future of the number of users imported.
     */
    @NotNull CompletableFuture<Integer> importUsers(@NotNull Map<UUID, String> users);

    int countUsers();

    Set<UserKey> listUsers();

    boolean hasUser(@NotNull UUID uuid);

    default boolean hasUser(@NotNull UserKey user) {
        return hasUser(user.uuid());
    }


}
