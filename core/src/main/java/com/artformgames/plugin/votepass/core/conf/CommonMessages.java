package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

@ConfigPath(root = true)
public class CommonMessages extends TextMessages {

    public static final ConfiguredMessageList<BaseComponent[]> NO_PERMISSION = list()
            .defaults("&c&lSorry! &fBut you dont have enough permissions to do that!")
            .build();

    public static final ConfiguredMessageList<String> LOAD_FAILED = ConfiguredMessageList.asStrings()
            .defaults("&c&lSorry! &fBut your whitelist data failed to load, please rejoin!")
            .build();

    public static final ConfiguredMessageList<String> ONLY_PLAYER = ConfiguredMessageList.asStrings()
            .defaults("&c&lSorry! &fBut this command only can be executed by a player!")
            .build();

    public static final class RELOAD extends TextMessages {

        public static final ConfiguredMessageList<String> START = ConfiguredMessageList.asStrings()
                .defaults("&fReloading the plugin configurations...")
                .build();

        public static final ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                .defaults("&a&lSuccess! &fThe plugin configurations has been reloaded, cost &a%(time)&fms.")
                .params("time")
                .build();

        public static final ConfiguredMessageList<String> FAILED = ConfiguredMessageList.asStrings()
                .defaults("&c&lFailed! &fThe plugin configurations failed to reload.")
                .build();

    }

}
