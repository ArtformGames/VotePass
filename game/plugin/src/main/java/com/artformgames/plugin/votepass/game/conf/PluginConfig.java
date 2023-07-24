package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredMap;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import com.artformgames.plugin.votepass.game.ui.admin.AdminHandleGUI;
import com.artformgames.plugin.votepass.game.ui.admin.AdminManageGUI;
import com.artformgames.plugin.votepass.game.ui.request.RequestCommentsGUI;
import com.artformgames.plugin.votepass.game.ui.user.AbstainToggleGUI;
import com.artformgames.plugin.votepass.game.ui.vote.QuickReviewGUI;
import com.artformgames.plugin.votepass.game.ui.vote.RequestListGUI;
import com.artformgames.plugin.votepass.game.ui.vote.VoteConfirmGUI;
import com.artformgames.plugin.votepass.game.ui.vote.VoteHandleGUI;

import java.time.Duration;

public class PluginConfig extends ConfigurationRoot {

    public static final class SERVER extends ConfigurationRoot {

        @HeaderComment({"The identify of this server", "Used for request and whitelist data"})
        public static final ConfiguredValue<String> ID = ConfiguredValue.of(String.class, "survival");


        @HeaderComment({
                "Auto pass ratio, when the ratio of the passed vote is greater than the value,",
                "the vote will be automatically passed, below zero means disabled auto pass.",
                "You can configure different ratio based on different total votable players.",
        })
        public static final ConfiguredMap<Integer, Double> AUTO_PASS_RATIO = ConfiguredMap.builderOf(Integer.class, Double.class)
                .asTreeMap().fromString().parseKey(Integer::parseInt).parseValue(Double::parseDouble)
                .defaults(m -> {
                    m.put(5, 0.8);
                    m.put(20, 0.6);
                }).build();

        @HeaderComment({
                "Active user last online time",
                "Only users that have been online within this time will be counted as active users.",
                "And only active users will be count when auto pass handling."
        })
        public static final ConfiguredValue<Duration> ACTIVE_ONLINE_TIME = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parseValue((v, d) -> TimeStringUtils.parseDuration(v))
                .serializeValue(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(7))
                .build();

        @HeaderComment({
                "Periods that sync the new requests",
                "This value should not be lower than 10 seconds.",
                "If periods value ≤0 , the new requests will only be synced by execute 'votepass sync' command"
        })
        public static final ConfiguredValue<Duration> SYNC_PERIOD = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parseValue((v, d) -> TimeStringUtils.parseDuration(v))
                .serializeValue(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofMinutes(3))
                .build();

        @HeaderComment({"Periods that notify players to handle requests"})
        public static final ConfiguredValue<Duration> NOTIFY_PERIOD = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parseValue((v, d) -> TimeStringUtils.parseDuration(v))
                .serializeValue(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofMinutes(15))
                .build();

        @HeaderComment({
                "The kick message when the player is not in the whitelist."
        })
        public static final ConfiguredMessageList<String> KICK_MESSAGE = ConfiguredMessageList.asStrings()
                .defaults("You are not in the whitelist, please request to join the whitelist.")
                .build();

        @HeaderComment("Whether to restrict administrators to managing only requests that require intervention.")
        public static final ConfiguredValue<Boolean> MANAGE_RESTRICT = ConfiguredValue.of(Boolean.class, false);

    }

    public static final class SOUNDS extends ConfigurationRoot {

        public static final ConfiguredSound NOTIFY = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        public static final ConfiguredSound ABSTAIN = ConfiguredSound.of("ENTITY_VILLAGER_HURT");

        public static final ConfiguredSound APPROVED = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        public static final ConfiguredSound REJECT = ConfiguredSound.of("ENTITY_VILLAGER_NO");

    }

    public static final class GUIS extends ConfigurationRoot {

        public static final Class<?> ABSTAIN_TOGGLE = AbstainToggleGUI.CONFIG.class;

        public static final Class<?> REQUEST_LIST = RequestListGUI.CONFIG.class;
        public static final Class<?> REQUEST_COMMENTS = RequestCommentsGUI.CONFIG.class;

        public static final Class<?> QUICK_REVIEW = QuickReviewGUI.CONFIG.class;
        public static final Class<?> VOTE_HANDLE = VoteHandleGUI.CONFIG.class;
        public static final Class<?> VOTE_CONFIRM = VoteConfirmGUI.CONFIG.class;

        public static final Class<?> ADMIN_MANAGE = AdminManageGUI.CONFIG.class;
        public static final Class<?> ADMIN_HANDLE = AdminHandleGUI.CONFIG.class;

    }


}
