package com.artformgames.plugin.votepass.api.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

public interface UserDataManager<U extends UserData> extends UserKeyManager {

    @NotNull CompletableFuture<U> load(@NotNull UserKey key, @NotNull BooleanSupplier cacheCondition);

    @NotNull CompletableFuture<U> load(@NotNull UUID uuid, @NotNull BooleanSupplier cacheCondition);

    default @NotNull CompletableFuture<U> load(@NotNull UUID uuid) {
        return load(uuid, false);
    }

    default @NotNull CompletableFuture<U> load(@NotNull UUID uuid, boolean cache) {
        return load(uuid, () -> cache);
    }

    @Unmodifiable
    @NotNull Set<U> list();

    default @NotNull U get(@NotNull UUID uuid) {
        return Optional.ofNullable(getNullable(uuid)).orElseThrow(() -> new NullPointerException("User " + uuid + " not loaded."));
    }

    @Nullable U getNullable(@NotNull UUID uuid);

    default @NotNull Optional<@Nullable U> getOptional(@NotNull UUID uuid) {
        return Optional.ofNullable(getNullable(uuid));
    }

    boolean save(@NotNull U user);

    default @NotNull CompletableFuture<Boolean> unload(@NotNull UUID uuid) {
        return unload(uuid, true);
    }

    @NotNull CompletableFuture<Boolean> unload(@NotNull UUID uuid, boolean save);

}
