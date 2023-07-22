package com.artformgames.plugin.votepass.game.api.whiteist;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface WhitelistModifier {

    @NotNull CompletableFuture<Integer> execute();

    WhitelistModifier modify(UserKey user, @Nullable Consumer<WhitelistUserModifier> migrator);

    default WhitelistModifier add(UserKey user) {
        return modify(user, null);
    }

    default WhitelistModifier update(WhitelistedUserData data) {
        return modify(
                data.getKey(),
                user -> user.setLinkedRequestID(data.getLinkedRequestID())
                        .setAbstained(data.isAbstained())
                        .setLastOnline(data.getLastOnline())
                        .setPassedTime(data.getPassedTime())
        );
    }

    default WhitelistModifier add(RequestInformation request) {
        return modify(
                request.getUser(),
                user -> user.setPassedTime(LocalDateTime.now()).setLinkedRequestID(request.getID())
        );
    }

    WhitelistModifier remove(UserKey user);
}