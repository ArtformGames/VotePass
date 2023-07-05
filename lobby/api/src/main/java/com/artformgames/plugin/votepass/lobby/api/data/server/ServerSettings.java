package com.artformgames.plugin.votepass.lobby.api.data.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.SortedMap;

public record ServerSettings(
        @NotNull String id, @NotNull String name,
        @Nullable ServerRules rules, @NotNull SortedMap<Integer, ServerQuestion> questions
) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerSettings that = (ServerSettings) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
