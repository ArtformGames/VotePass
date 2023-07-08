package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
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
        public static final ConfiguredList<String> KICK_MESSAGE = ConfiguredList.builderOf(String.class)
                .fromString()
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

        public static final class PAGE_ITEMS extends ConfigurationRoot {

            public static final ConfiguredItem PREVIOUS_PAGE = ConfiguredItem.create()
                    .defaults(Material.ARROW, "&fPrevious page")
                    .defaultLore(
                            " ",
                            "&f  Left click to view the previous page.",
                            "&f  Right click to view the first page.",
                            " ")
                    .build();

            public static final ConfiguredItem NEXT_PAGE = ConfiguredItem.create()
                    .defaults(Material.ARROW, "&fNext page")
                    .defaultLore(
                            " ",
                            "&f  Left click to view the next page.",
                            "&f  Right click to view the last page.",
                            " "
                    ).build();

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

        public static final ConfiguredItem PENDING = ConfiguredItem.create()
                .defaultType(Material.BARRIER)
                .defaultName("&e&lPlease answer all the questions")
                .defaultLore(
                        " ",
                        "&fAfter answered all the questions,",
                        "&fthat you can submit your request to the server users."
                ).build();

    }


}
