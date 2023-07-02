package com.artformgames.plugin.votepass.api.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface UserKeyManager {

    enum KeyType {
        ID("id"), UUID("uuid"), NAME("name");

        final String columnName;

        KeyType(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }
    }

    /**
     * Get the user's key information by the specified type.
     *
     * @param type  {@link KeyType}
     * @param param The query param of the type
     * @return {@link UserKey}
     */
    @Nullable UserKey getKey(KeyType type, Object param);

    /**
     * Get the user's key information by the specified type.
     * If there's no key information, create a new one in database.
     *
     * @param uuid     User UUID
     * @param username Username
     * @return {@link UserKey}
     */
    @NotNull UserKey upsertKey(@NotNull UUID uuid, @Nullable String username) throws Exception;

    default @Nullable UserKey getKey(int id) {
        return getKey(KeyType.ID, id);
    }

    default @Nullable UserKey getKey(@NotNull UUID uuid) {
        return getKey(KeyType.UUID, uuid);
    }

    default @Nullable UserKey getKey(@NotNull String username) {
        return getKey(KeyType.NAME, username);
    }

    default @Nullable Long getID(@NotNull String username) {
        return Optional.ofNullable(getKey(username)).map(UserKey::id).orElse(null);
    }

    default @Nullable Long getID(@NotNull UUID userUUID) {
        return Optional.ofNullable(getKey(userUUID)).map(UserKey::id).orElse(null);
    }


    default @Nullable String getUsername(int id) {
        return Optional.ofNullable(getKey(id)).map(UserKey::name).orElse(null);
    }

    default @Nullable String getUsername(@NotNull UUID userUUID) {
        return Optional.ofNullable(getKey(userUUID)).map(UserKey::name).orElse(null);
    }

    default @Nullable UUID getUUID(int id) {
        return Optional.ofNullable(getKey(id)).map(UserKey::uuid).orElse(null);
    }

    default @Nullable UUID getUUID(@NotNull String username) {
        return Optional.ofNullable(getKey(username)).map(UserKey::uuid).orElse(null);
    }

}
