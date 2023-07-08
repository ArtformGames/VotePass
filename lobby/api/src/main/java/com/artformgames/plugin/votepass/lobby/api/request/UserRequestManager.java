package com.artformgames.plugin.votepass.lobby.api.request;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface UserRequestManager {

    @NotNull CompletableFuture<RequestInformation> commit(@NotNull UserKey user, @NotNull PendingRequest request);

    @NotNull CompletableFuture<Void> update(@NotNull RequestInformation content);

}
