package com.artformgames.plugin.votepass.game.ui;

import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.user.UsersManager;

import java.text.NumberFormat;
import java.util.UUID;

public record RequestIconInfo(
        RequestInformation info,
        int words, int total, int size,
        int pros, int cons, int abs,
        int passRequired, int passRemain,
        String prosPercent, String consPercent, String absPercent,
        String createTime, String expireTime
) {
    public static RequestIconInfo of(RequestInformation info) {
        UsersManager manager = Main.getInstance().getUserManager();

        int words = info.countAnswerWords();
        int countable = manager.countUser(WhitelistedUserData::isVoteCountable);   // General counting.

        int voteSize = info.count(null);
        int pros = 0;
        int abs = 0;
        int cons = 0;
        int missed = 0;

        for (VoteInformation vote : info.getVotes()) {
            switch (vote.decision()) {
                case ABSTAIN -> abs++;
                case APPROVE -> pros++;
                case REJECT -> cons++;
            }
            WhitelistedUserData data = manager.getWhitelistData(vote.voter());
            if (data != null && !data.isVoteCountable()) {
                missed++; // Data is uncountable but exists in votes, missed in general counting.
            }
        }

        int total = countable + missed - abs;
        double ratio = Main.getInstance().getVoteManager().getAutoPassRatio(total);
        int autoApprove = (int) (total * Math.min(1, Math.max(0, ratio)));

        return new RequestIconInfo(
                info, words, total, voteSize, pros, cons, abs,
                autoApprove, autoApprove - pros,
                getPercent(pros, voteSize), getPercent(cons, voteSize), getPercent(abs, voteSize),
                info.getCreateTimeString(),
                info.getExpireTimeString(CommonConfig.TIME.AUTO_CLOSE.getNotNull())
        );
    }

    public Object[] generateParams() {
        return new Object[]{
                displayName(), uuid(), id(), words(), createTime(), expireTime(),
                pros(), prosPercent(), cons(), consPercent(), abs(), absPercent(),
                passRequired(), passRemain(), size(), total()
        };
    }

    public ConfiguredItem.PreparedItem prepareIcon() {
        return PluginConfig.ICON.INFO
                .prepare(generateParams())
                .setSkullOwner(info.getUserUUID());
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

    public static String getPercent(int x, int y) {
        if (x <= 0 || y <= 0) return "0.00";

        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getInstance();
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format((d1 / d2) * 100);
    }
}
