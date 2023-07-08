package com.artformgames.plugin.votepass.game.migrator;

import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistUserModifier;
import com.artformgames.plugin.votepass.game.user.WhitelistUser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class WhitelistUserModifierImpl implements WhitelistUserModifier {

    protected final @NotNull UserKey key;

    Integer request;
    boolean abstained;
    LocalDateTime passedTime;
    LocalDateTime lastOnline;

    public WhitelistUserModifierImpl(@NotNull UserKey key) {
        this.key = key;
    }

    @Override
    public WhitelistUserModifier setLinkedRequestID(@Nullable Integer id) {
        this.request = id;
        return this;
    }

    @Override
    public WhitelistUserModifier setAbstained(boolean abstained) {
        this.abstained = abstained;
        return this;
    }

    @Override
    public WhitelistUserModifier setPassedTime(@NotNull LocalDateTime time) {
        this.passedTime = time;
        return this;
    }

    @Override
    public WhitelistUserModifier setLastOnline(@Nullable LocalDateTime time) {
        this.lastOnline = time;
        return this;
    }

    public WhitelistUser toUser() {
        return new WhitelistUser(key, request, abstained, passedTime, lastOnline);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WhitelistUserModifierImpl that = (WhitelistUserModifierImpl) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
