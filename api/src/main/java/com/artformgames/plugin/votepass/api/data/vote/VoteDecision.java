package com.artformgames.plugin.votepass.api.data.vote;

import java.util.Arrays;

public enum VoteDecision {

    ABSTAIN(1),
    APPROVE(2),
    REJECT(3);

    private final int id;

    VoteDecision(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public static VoteDecision parse(int id) {
        return Arrays.stream(VoteDecision.values()).filter(r -> r.id == id).findFirst().orElse(null);
    }

}
