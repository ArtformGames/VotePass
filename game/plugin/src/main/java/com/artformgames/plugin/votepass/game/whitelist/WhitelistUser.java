package com.artformgames.plugin.votepass.game.whitelist;

import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.user.AbstractUserData;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

public class WhitelistUser extends AbstractUserData implements WhitelistedUserData {

    protected final @Nullable Integer request;
    protected final @NotNull LocalDateTime passedTime;

    protected @Nullable LocalDateTime lastOnline;
    protected boolean abstained;

    public WhitelistUser(@NotNull UserKey key,
                            @Nullable Integer request, boolean abstained,
                            @NotNull LocalDateTime passedTime, @Nullable LocalDateTime lastOnline) {
        super(key);
        this.request = request;
        this.abstained = abstained;
        this.passedTime = passedTime;
        this.lastOnline = lastOnline;
    }

    @Override
    public @Nullable Integer getLinkedRequestID() {
        return this.request;
    }

    @Override
    public boolean isAbstained() {
        return this.abstained;
    }

    @Override
    public boolean isInactive() {
        if (getLastOnline() == null) return true;

        Duration active = PluginConfig.SERVER.ACTIVE_ONLINE_TIME.get();
        if (active == null) return false; // means no active time limit

        LocalDateTime last = getLastOnline();
        LocalDateTime expired = last.plus(active);

        return LocalDateTime.now().isAfter(expired);
    }

    @Override
    public void setAbstained(boolean abstained) {
        this.abstained = abstained;
    }

    @Override
    public @Nullable LocalDateTime getLastOnline() {
        if (Bukkit.getPlayer(getUserUUID()) != null) return LocalDateTime.now();
        else return this.lastOnline;
    }

    @Override
    public void setLastOnline(@Nullable LocalDateTime lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public @NotNull LocalDateTime getPassedTime() {
        return this.passedTime;
    }

}
