package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import com.artformgames.plugin.votepass.core.database.DBConfiguration;
import org.bukkit.Material;

public interface CommonConfig extends Configuration {

    @ConfigPath(root = true)
    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComments({
            "Statistics Settings",
            "This option is used to help developers count plug-in versions and usage, and it will never affect performance and user experience.",
            "Of course, you can also choose to turn it off here for this plugin,",
            "or turn it off for all plugins in the configuration file under \"plugins/bStats\"."
    })
    @ConfigPath(root = true)
    ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "Check update settings",
            "This option is used by the plug-in to determine whether to check for updates.",
            "If you do not want the plug-in to check for updates and prompt you, you can choose to close.",
            "Checking for updates is an asynchronous operation that will never affect performance and user experience."
    })
    @ConfigPath(root = true)
    ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @ConfigPath(root = true)
    Class<DBConfiguration> DATABASE = DBConfiguration.class;

    @ConfigPath(root = true)
    interface PAGE_ITEMS extends Configuration {

        ConfiguredItem PREVIOUS_PAGE = ConfiguredItem.create()
                .defaultType(Material.ARROW)
                .defaultName("&fPrevious page")
                .defaultLore(
                        " ",
                        "&f  Left click to view the previous page.",
                        "&f  Right click to view the first page.",
                        " ")
                .build();

        ConfiguredItem NEXT_PAGE = ConfiguredItem.create()
                .defaultType(Material.ARROW)
                .defaultName("&fNext page")
                .defaultLore(
                        " ",
                        "&f  Left click to view the next page.",
                        "&f  Right click to view the last page.",
                        " "
                ).build();

    }


}

