package com.artformgames.plugin.votepass.server.api.user;

import com.artformgames.plugin.votepass.api.user.UserData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public interface GameUserData extends UserData {

    @Nullable Integer getLinkedRequest();

    boolean isAbstained();

    boolean isVoteCountable();

    @Nullable LocalDateTime getLastOnline();

    @NotNull LocalDateTime getPassedTime();

}
