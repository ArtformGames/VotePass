package com.artformgames.plugin.votepass.lobby.request;

import com.artformgames.plugin.votepass.api.data.request.RequestInfo;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class RequestManager implements UserRequestManager {

    @Override
    public @NotNull CompletableFuture<RequestInfo> commit(@NotNull UserKey user, @NotNull PendingRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int id = Main.getInstance().getDataManager().createRequest(
                        request.getSettings().id(), user.id(), request.getAnswers()
                );
                return new RequestInfo(
                        id, request.getSettings().id(), user,
                        request.getAnswers(), new HashSet<>(),
                        null, RequestResult.PENDING, false,
                        LocalDateTime.now(), null
                );
            } catch (SQLException e) {
                Main.severe("Error occurred when committing " + user.name() + "'s request, please check the configuration.");
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public @NotNull CompletableFuture<Void> update(@NotNull RequestInfo content) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Main.getInstance().getDataManager().updateRequest(content);
                return null;
            } catch (Exception ex) {
                Main.severe("Error occurred when updating #" + content.getID() + " request, please check the configuration.");
                ex.printStackTrace();
                return null;
            }
        });
    }
}
