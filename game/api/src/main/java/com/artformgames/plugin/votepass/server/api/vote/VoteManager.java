package com.artformgames.plugin.votepass.server.api.vote;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.api.data.vote.VoteInfomation;
import com.artformgames.plugin.votepass.server.api.user.VoteUserData;

import java.util.SortedMap;

public interface VoteManager {


    int sync();

    SortedMap<Integer, RequestInformation> getRequests();

    VoteInfomation submitVote(VoteUserData voter, PendingVote pendingVote);

    RequestResult calculateResult(int pros, int cons);

    void approve(RequestInformation request);

    void reject(RequestInformation request);

    void updateResult(RequestInformation request, RequestResult result);

}
