package com.artformgames.plugin.votepass.api.user;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface UserData {

    @NotNull UserKey getKey();

    default long getUID() {
        return getKey().id();
    }

    default @NotNull UUID getUserUUID() {
        return getKey().uuid();
    }


}
