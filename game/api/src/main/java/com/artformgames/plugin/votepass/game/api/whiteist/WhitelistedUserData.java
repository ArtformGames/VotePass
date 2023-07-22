package com.artformgames.plugin.votepass.game.api.whiteist;

import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.api.utils.TimeFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public interface WhitelistedUserData extends UserData {

    @Nullable Integer getLinkedRequestID();

    boolean isAbstained();

    boolean isInactive();

    void setAbstained(boolean abstained);

    default boolean isVoteCountable() {
        return !isAbstained() && !isInactive();
    }

    @Nullable LocalDateTime getLastOnline();

    void setLastOnline(@Nullable LocalDateTime lastOnline);

    default void updateLastOnline() {
        setLastOnline(LocalDateTime.now());
    }

    @NotNull LocalDateTime getPassedTime();

    default @NotNull String getPassedTimeString() {
        return TimeFormatUtils.formatTime(getPassedTime());
    }

}
