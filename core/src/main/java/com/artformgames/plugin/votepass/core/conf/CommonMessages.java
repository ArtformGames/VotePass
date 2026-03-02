package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import net.md_5.bungee.api.chat.BaseComponent;

@ConfigPath(root = true)
public interface CommonMessages extends TextMessages {

    ConfiguredMessage<BaseComponent[]> NO_PERMISSION = TextMessages.create()
            .defaults("&c&lSorry! &fBut you dont have enough permissions to do that!")
            .build();

    ConfiguredMessage<String> LOAD_FAILED = ConfiguredMessage.asString()
            .defaults("&c&lSorry! &fBut your whitelist data failed to load, please rejoin!")
            .build();

    ConfiguredMessage<BaseComponent[]> ONLY_PLAYER = TextMessages.create()
            .defaults("&c&lSorry! &fBut this command only can be executed by a player!")
            .build();


    ConfiguredMessage<BaseComponent[]> WRONG_NUMBER = TextMessages.create()
            .defaults("&c&lSorry! &fBut please input a correct number!")
            .build();


    ConfiguredMessage<BaseComponent[]> NOT_EXISTS = TextMessages.create()
            .defaults("&c&lSorry! &fThere is currently no pending request with ID &e#%(id) &f!")
            .params("id")
            .build();


    interface RELOAD extends TextMessages {

        ConfiguredMessage<BaseComponent[]> START = TextMessages.create()
                .defaults("&fReloading the plugin configurations...")
                .build();

        ConfiguredMessage<BaseComponent[]> SUCCESS = TextMessages.create()
                .defaults("&a&lSuccess! &fThe plugin configurations has been reloaded, cost &a%(time)&fms.")
                .params("time")
                .build();

        ConfiguredMessage<BaseComponent[]> FAILED = TextMessages.create()
                .defaults("&c&lFailed! &fThe plugin configurations failed to reload.")
                .build();

    }

}
