package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import com.artformgames.plugin.votepass.core.database.DBConfiguration;
import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import org.bukkit.Material;

import java.time.Duration;

public class CommonConfig extends ConfigurationRoot{

    @ConfigPath(root = true)
    public static final ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "Statistics Settings",
            "This option is used to help developers count plug-in versions and usage, and it will never affect performance and user experience.",
            "Of course, you can also choose to turn it off here for this plugin,",
            "or turn it off for all plugins in the configuration file under \"plugins/bStats\"."
    })
    @ConfigPath(root = true)
    public static final ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "Check update settings",
            "This option is used by the plug-in to determine whether to check for updates.",
            "If you do not want the plug-in to check for updates and prompt you, you can choose to close.",
            "Checking for updates is an asynchronous operation that will never affect performance and user experience."
    })
    @ConfigPath(root = true)
    public static final ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @ConfigPath(root = true)
    public static final Class<DBConfiguration> DATABASE = DBConfiguration.class;

    @ConfigPath(root = true)
    public static final class TIME extends ConfigurationRoot {

        public static final ConfiguredValue<Duration> AUTO_CLOSE = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parseValue((v, d) -> TimeStringUtils.parseDuration(v))
                .serializeValue(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(15))
                .build();

        public static final ConfiguredValue<Duration> ADMIN_INTERVENTION = ConfiguredValue
                .builderOf(Duration.class).fromString()
                .parseValue((v, d) -> TimeStringUtils.parseDuration(v))
                .serializeValue(TimeStringUtils::serializeDuration)
                .defaults(Duration.ofDays(7))
                .build();

    }

    @ConfigPath(root = true)
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


}

