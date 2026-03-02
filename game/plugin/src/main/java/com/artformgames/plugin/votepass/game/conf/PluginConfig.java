package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredMap;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import com.artformgames.plugin.votepass.game.ui.admin.AdminHandleGUI;
import com.artformgames.plugin.votepass.game.ui.admin.AdminManageGUI;
import com.artformgames.plugin.votepass.game.ui.request.RequestCommentsGUI;
import com.artformgames.plugin.votepass.game.ui.user.AbstainToggleGUI;
import com.artformgames.plugin.votepass.game.ui.vote.RequestListGUI;
import com.artformgames.plugin.votepass.game.ui.vote.VoteConfirmGUI;
import com.artformgames.plugin.votepass.game.ui.vote.VoteHandleGUI;
import org.bukkit.Material;

import java.time.Duration;
import java.util.Objects;

@ConfigPath(root = true)
public interface PluginConfig extends Configuration {

    interface TIME extends Configuration {

        @HeaderComments("The time that the request will be automatically closed if it have not been handled yet.")
        ConfiguredValue<Duration> AUTO_CLOSE = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parse(d -> Objects.requireNonNull(TimeStringUtils.parseDuration(d)))
                .serialize(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(15))
                .build();

        @HeaderComments("The time that the request will be sent to admin to handle if request still have no result.")
        ConfiguredValue<Duration> ADMIN_INTERVENTION = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parse(d -> Objects.requireNonNull(TimeStringUtils.parseDuration(d)))
                .serialize(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(7))
                .build();
    }

    interface SERVER extends Configuration {

        @HeaderComments({"The identify of this server", "Used for request and whitelist data"})
        ConfiguredValue<String> ID = ConfiguredValue.of(String.class, "survival");


        @HeaderComments({
                "Auto pass ratio, when the ratio of the passed vote is greater than the value,",
                "the vote will be automatically passed, below zero means disabled auto pass.",
                "You can configure different ratio based on different size votable players.",
        })
        ConfiguredMap<Integer, Double> AUTO_PASS_RATIO = ConfiguredMap.builderOf(Integer.class, Double.class)
                .asTreeMap().fromString()
                .parseKey(s -> Integer.parseInt(s))
                .parse(Double::parseDouble)
                .defaults(m -> {
                    m.put(2, 1.0);
                    m.put(5, 0.8);
                    m.put(20, 0.6);
                }).build();

        @HeaderComments({
                "Active user last online time",
                "Only users that have been online within this time will be counted as active users.",
                "And only active users will be count when auto pass handling."
        })
        ConfiguredValue<Duration> ACTIVE_ONLINE_TIME = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parse(d -> Objects.requireNonNull(TimeStringUtils.parseDuration(d)))
                .serialize(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(7))
                .build();

        @HeaderComments({
                "Periods that sync the new requests",
                "This value should not be lower than 10 seconds.",
                "If periods value ≤0 , the new requests will only be synced by execute 'votepass sync' command"
        })
        ConfiguredValue<Duration> SYNC_PERIOD = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parse(d -> Objects.requireNonNull(TimeStringUtils.parseDuration(d)))
                .serialize(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofMinutes(3))
                .build();

        @HeaderComments({"Periods that notify players to handle requests"})
        ConfiguredValue<Duration> NOTIFY_PERIOD = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parse(d -> Objects.requireNonNull(TimeStringUtils.parseDuration(d)))
                .serialize(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofMinutes(15))
                .build();

        @HeaderComments({
                "The kick message when the player is not in the whitelist."
        })
        ConfiguredMessage<String> KICK_MESSAGE = ConfiguredMessage.asString()
                .defaults("You are not in the whitelist, please request to join the whitelist.")
                .build();

        @HeaderComments("Whether to restrict administrators to managing only requests that require intervention.")
        ConfiguredValue<Boolean> MANAGE_RESTRICT = ConfiguredValue.of(Boolean.class, false);

        @HeaderComments({
                "The commands that will be executed after player submit a vote."
        })
        interface COMMANDS extends Configuration {

            ConfiguredMessage<String> APPROVE = ConfiguredMessage.asString()
                    .defaults("say &a%player_name% &fjust &aapproved&f the &a%(target)&f's request &6#%(id) &f!")
                    .params("id", "target")
                    .build();

            ConfiguredMessage<String> ABSTAIN = ConfiguredMessage.asString()
                    .defaults("say &a%player_name% &fjust &eabstained&f to vote for the &a%(target)&f's request &6#%(id) &f!")
                    .params("id", "target")
                    .build();

            ConfiguredMessage<String> REJECT = ConfiguredMessage.asString()
                    .defaults("say &a%player_name% &fjust &cdenied&f the &a%(target)&f's request &6#%(id) &f!")
                    .params("id", "target")
                    .build();

        }


    }

    interface SOUNDS extends Configuration {

        ConfiguredSound NOTIFY = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        ConfiguredSound ABSTAIN = ConfiguredSound.of("ENTITY_VILLAGER_HURT");

        ConfiguredSound APPROVED = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        ConfiguredSound REJECT = ConfiguredSound.of("ENTITY_VILLAGER_NO");

    }

    interface COMMENT extends Configuration {

        @HeaderComments("Max letters in a single comment")
        ConfiguredValue<Integer> MAX = ConfiguredValue.of(Integer.class, 120);

        @HeaderComments("How many letters are displayed in a single line")
        ConfiguredValue<Integer> LINE = ConfiguredValue.of(Integer.class, 25);


    }

    interface ANSWERS extends Configuration {

        @HeaderComments("Hidden questions' id list, will not be displayed to voters.")
        ConfiguredList<Integer> HIDDEN = ConfiguredList.of(999);

        @HeaderComments("How many letters are displayed in a single line")
        ConfiguredValue<Integer> LETTERS_PER_LINE = ConfiguredValue.of(Integer.class, 25);

        @HeaderComments("Max lines that displayed in lore")
        ConfiguredValue<Integer> MAX_LINES = ConfiguredValue.of(Integer.class, 6);

        @HeaderComments("Extra lore if answers are too long, tell voters to click to view details.")
        ConfiguredMessage<String> EXTRA = ConfiguredMessage.ofString(
                "&f&o  ... More in details!"
        );

    }


    interface ICON extends Configuration {

        ConfiguredItem INFO = ConfiguredItem.create()
                .defaultType(Material.PLAYER_HEAD)
                .defaultName("&7#%(request_id) &e&l%(name)")
                .defaultLore(
                        " ",
                        "&7Request form &e&l%(name)",
                        "&7UUID: &e%(uuid)",
                        "&7",
                        "&7Contain words: &e%(request_words)",
                        "&7Submit time: &e%(create_time)",
                        "&7Close time: &e%(close_time)",
                        " ",
                        "&f✔ &a&lApproved&7: &a%(pros_amount)&7/%(votes_amount) &8(%(pros_ratio)%)",
                        "&f✘ &c&lRejected&7: &c%(cons_amount)&7/%(votes_amount) &8(%(cons_ratio)%)",
                        "&f◮ &e&lAbstained&7: &7%(abstains_amount)&7/%(votes_amount) &8(%(abstains_ratio)%)",
                        " ",
                        "&fRequire &e%(pass_remain)&7/%(pass_required) &fmore approves to pass the request.",
                        "#click-lore#{1,0}"
                ).params("name", "uuid",
                        "request_id", "request_words",
                        "create_time", "close_time",
                        "pros_amount", "pros_ratio",
                        "cons_amount", "cons_ratio",
                        "abstains_amount", "abstains_ratio",
                        "pass_required", "pass_remain",
                        "votes_amount", "total_amount"
                ).build();

    }

    @SuppressWarnings("unused")
    interface GUIS extends Configuration {

        Class<?> ABSTAIN_TOGGLE = AbstainToggleGUI.CONFIG.class;

        Class<?> REQUEST_LIST = RequestListGUI.CONFIG.class;
        Class<?> REQUEST_COMMENTS = RequestCommentsGUI.CONFIG.class;

        Class<?> VOTE_HANDLE = VoteHandleGUI.CONFIG.class;
        Class<?> VOTE_CONFIRM = VoteConfirmGUI.CONFIG.class;

        Class<?> ADMIN_MANAGE = AdminManageGUI.CONFIG.class;
        Class<?> ADMIN_HANDLE = AdminHandleGUI.CONFIG.class;

    }


}
