package com.artformgames.plugin.votepass.game.api.vote;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.game.api.user.GameUserData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.SortedMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public interface VoteManager {

    int sync();

    double getAutoPassRatio(int total);

    @Unmodifiable
    @NotNull SortedMap<Integer, RequestInformation> getRequests();

    @Nullable RequestInformation getRequest(int requestID);

    int countRequest(@Nullable Predicate<RequestInformation> predicate);

    int countAdminRequests();

    @NotNull CompletableFuture<VoteInformation> submitVote(GameUserData voter, PendingVote pendingVote);

    @NotNull RequestResult calculateResult(@NotNull RequestInformation request);

    CompletableFuture<Boolean> approve(@NotNull RequestInformation request);

    CompletableFuture<Boolean> reject(@NotNull RequestInformation request);

    CompletableFuture<Boolean> updateResult(@NotNull RequestInformation request, @NotNull RequestResult result);

}
