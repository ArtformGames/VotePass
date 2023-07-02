package com.artformgames.plugin.votepass.lobby.api.data.server;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ServerQuestion(@NotNull String title,@NotNull  List<String> description) {
}
