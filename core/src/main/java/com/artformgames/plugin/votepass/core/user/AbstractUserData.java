package com.artformgames.plugin.votepass.core.user;

import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.api.user.UserKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractUserData implements UserData {
    protected final @NotNull UserKey key;

    protected AbstractUserData(@NotNull UserKey key) {
        this.key = key;
    }

    public @NotNull UserKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUserData userData = (AbstractUserData) o;
        return key.equals(userData.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
