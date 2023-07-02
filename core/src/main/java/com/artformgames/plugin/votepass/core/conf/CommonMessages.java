package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.core.annotation.ConfigPath;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import net.md_5.bungee.api.chat.BaseComponent;

@ConfigPath(root = true)
public class CommonMessages extends TextMessages {

    public static final ConfiguredMessageList<BaseComponent[]> NO_PERMISSION = list()
            .defaults("&c&lSorry! &fBut you dont have enough permissions to do that!")
            .build();


}
