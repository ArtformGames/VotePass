package com.artformgames.plugin.votepass.api.data.request;

import java.util.Arrays;

public enum RequestResult {

    PENDING(0),
    APPROVED(1),
    REJECTED(2),
    EXPIRED(3);

    private final int id;

    RequestResult(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public static RequestResult parse(int id) {
        return Arrays.stream(RequestResult.values()).filter(r -> r.id == id).findFirst().orElse(null);
    }

}
