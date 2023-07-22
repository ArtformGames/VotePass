package com.artformgames.plugin.votepass.game.listener;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        WhitelistedUserData user = Main.getInstance().getUserManager().getWhitelistData(player.getUniqueId());
        if (user == null) return;

        // Auto toggle abstain status if the user doesn't have the permission
        if (!player.hasPermission("VotePass.abstain") && user.isAbstained()) {
            user.setAbstained(false);
        }
    }

}
