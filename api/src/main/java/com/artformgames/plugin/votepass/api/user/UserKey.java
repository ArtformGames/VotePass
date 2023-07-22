package com.artformgames.plugin.votepass.api.user;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public record UserKey(long id, @NotNull UUID uuid, @Nullable String name) {
    private static final Gson GSON = new Gson();

    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(uuid());
    }

    public @NotNull String getDisplayName() {
        return Optional.ofNullable(name()).orElse("?");
    }

    public String getValue(UserKeyManager.KeyType type) {
        return switch (type) {
            case ID -> String.valueOf(id);
            case UUID -> uuid.toString();
            case NAME -> name;
        };
    }

    public boolean equals(UserKeyManager.KeyType type, Object param) {
        return switch (type) {
            case ID -> param instanceof Long uid && id == uid;
            case UUID -> param instanceof UUID userUUID && uuid.equals(userUUID);
            case NAME -> param instanceof String name && name.equalsIgnoreCase(name);
        };
    }

    public Map<String, String> toMap() {
        String jsonValue = GSON.toJson(this);
        return Arrays.stream(UserKeyManager.KeyType.values())
                .collect(Collectors.toMap(this::getValue, value -> jsonValue, (a, b) -> b));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserKey userKey = (UserKey) o;
        return id == userKey.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
