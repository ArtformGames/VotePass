package com.artformgames.plugin.votepass.lobby.api.data.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.SortedMap;

public record ServerApplication(
        @NotNull String id, @NotNull String name,
        @Nullable ServerRules rules, @NotNull SortedMap<Integer, ServerQuestion> questions
) {


}
