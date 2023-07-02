package com.artformgames.plugin.votepass.lobby.api.data.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ServerRules(@Nullable String title, @Nullable String author,
                          @NotNull List<List<String>> pages) {

}
