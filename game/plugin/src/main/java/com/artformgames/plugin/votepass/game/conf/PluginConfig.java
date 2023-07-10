package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import org.bukkit.Material;

import java.time.Duration;

public class PluginConfig extends ConfigurationRoot {

    public static final Class<?> COMMON = CommonConfig.class;

    public static final class SERVER extends ConfigurationRoot {

        @HeaderComment({"The identify of this server", "Used for request and whitelist data"})
        public static final ConfiguredValue<String> ID = ConfiguredValue.of(String.class, "survival");

        @HeaderComment({
                "When the number of approved users on the server is more than this value,",
                "the vote will be automatically passed.",
        })
        public static final ConfiguredValue<Double> PASS_RATIO = ConfiguredValue.of(Double.class, 0.6);

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
                "The kick message when the player is not in the whitelist."
        })
        public static final ConfiguredMessageList<String> KICK_MESSAGE = ConfiguredMessageList.asStrings()
                .defaults("You are not in the whitelist, please request to join the whitelist.")
                .build();

    }

    public static final class SOUNDS extends ConfigurationRoot {

        public static final ConfiguredSound NOTIFY = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        public static final ConfiguredSound ABSTAIN = ConfiguredSound.of("ENTITY_VILLAGER_HURT");

        public static final ConfiguredSound APPROVED = ConfiguredSound.of("ENTITY_VILLAGER_CELEBRATE");

        public static final ConfiguredSound REJECT = ConfiguredSound.of("ENTITY_VILLAGER_NO");

    }

    public static final class GUIS extends ConfigurationRoot {

        public static final class LIST extends ConfigurationRoot {

            public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                    .defaults("&a&lAll Requests")
                    .build();

            public static final class ITEMS extends ConfigurationRoot {

                public static final ConfiguredItem INFO = ConfiguredItem.create()
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
                                "&a ▶ Left  click &8|&f view details",
                                "&a ▶ Right click &8|&f quick review"
                        ).params("name", "uuid",
                                "request_id", "request_words",
                                "create_time", "close_time",
                                "pros_amount", "pros_ratio",
                                "cons_amount", "cons_ratio",
                                "abstains_amount", "abstains_ratio",
                                "votes_amount"
                        ).build();

                public static final ConfiguredItem HISTORY = ConfiguredItem.create()
                        .defaultType(Material.CHEST_MINECART)
                        .defaultName("&e&lVote history")
                        .defaultLore(
                                " ",
                                "&7 Requests that you have been voted ,",
                                "&7 but not yet resolved will be shown here.",
                                " ",
                                "&a ▶ Click to view %(amount) requests."
                        ).params("amount").build();

            }


        }

        public static final class INFO extends ConfigurationRoot {


        }

        public static final class COMMENTS extends ConfigurationRoot {


        }

        public static final class VOTES extends ConfigurationRoot {


        }

        public static final class CONFIRM extends ConfigurationRoot {


        }


        public static final class HISTORY extends ConfigurationRoot {


        }

        public static final class ADMIN extends ConfigurationRoot {


        }

        public static final class HANDLE extends ConfigurationRoot {


        }

        public static final ConfiguredItem CONFIRM = ConfiguredItem.create()
                .defaultType(Material.EMERALD)
                .defaultName("&a&lConfirm and submit the request")
                .defaultLore(" ", "&fClick to confirm and submit your request.")
                .build();

        public static final ConfiguredItem CANCEL = ConfiguredItem.create()
                .defaultType(Material.REDSTONE)
                .defaultName("&c&lCancel the request")
                .defaultLore(" ", "&fClick to cancel your request.")
                .build();
    }


}
