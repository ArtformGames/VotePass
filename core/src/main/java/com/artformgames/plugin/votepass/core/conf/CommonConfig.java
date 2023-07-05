package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import com.artformgames.plugin.votepass.core.database.DBConfiguration;
import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;

import java.time.Duration;

@ConfigPath(root = true)
public class CommonConfig {

    public static final ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "Statistics Settings",
            "This option is used to help developers count plug-in versions and usage, and it will never affect performance and user experience.",
            "Of course, you can also choose to turn it off here for this plugin,",
            "or turn it off for all plugins in the configuration file under \"plugins/bStats\"."
    })
    public static final ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "Check update settings",
            "This option is used by the plug-in to determine whether to check for updates.",
            "If you do not want the plug-in to check for updates and prompt you, you can choose to close.",
            "Checking for updates is an asynchronous operation that will never affect performance and user experience."
    })
    public static final ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    public static final Class<DBConfiguration> DATABASE = DBConfiguration.class;

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

}

