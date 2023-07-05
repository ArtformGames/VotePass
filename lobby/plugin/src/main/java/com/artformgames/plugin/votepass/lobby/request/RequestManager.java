package com.artformgames.plugin.votepass.lobby.request;

import com.artformgames.plugin.votepass.api.data.request.RequestInfo;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RequestManager implements UserRequestManager {

    @Override
    public @NotNull CompletableFuture<RequestInfo> commit(@NotNull UserKey user, @NotNull PendingRequest request) {
        return null;
    }

    @Override
    public @NotNull CompletableFuture<Integer> update(@NotNull RequestInfo content) {
        return null;
    }
}
