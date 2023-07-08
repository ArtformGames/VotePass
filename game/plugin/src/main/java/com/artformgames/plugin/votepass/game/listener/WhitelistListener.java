package com.artformgames.plugin.votepass.game.listener;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class WhitelistListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        UUID playerUUID = event.getUniqueId();
        if (Main.getInstance().getUserManager().isWhitelisted(playerUUID)) return;

        // not whitelisted , kick and notify.
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST);

        String kickMessage = PluginConfig.SERVER.KICK_MESSAGE.parseToLine(null);
        if (kickMessage != null) event.setKickMessage(kickMessage);

    }

}
