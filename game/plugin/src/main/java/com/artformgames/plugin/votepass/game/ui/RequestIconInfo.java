package com.artformgames.plugin.votepass.game.ui;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;

import java.text.NumberFormat;
import java.util.UUID;

public record RequestIconInfo(
        RequestInformation info,
        int words, int total, int pros, int cons, int abs,
        String prosPercent, String consPercent, String absPercent,
        String createTime, String expireTime
) {
    public static RequestIconInfo of(RequestInformation info) {
        int total = info.count(null);
        int pros = info.count(VoteDecision.APPROVE);
        int abs = info.count(VoteDecision.ABSTAIN);
        int cons = total - pros - abs;

        int words = info.countAnswerWords();
        return new RequestIconInfo(
                info, words, total, pros, cons, abs,
                getPercent(pros, total), getPercent(cons, total), getPercent(abs, total),
                info.getCreateTimeString(),
                info.getExpireTimeString(CommonConfig.TIME.AUTO_CLOSE.getNotNull())
        );
    }

    public String displayName() {
        return info.getUser().getDisplayName();
    }

    public UUID uuid() {
        return info.getUser().uuid();
    }

    public int id() {
        return info.getID();
    }

    static String getPercent(int x, int y) {
        if (x == 0 || y == 0) return "0.00";

        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(d1 / d2);
    }
}
