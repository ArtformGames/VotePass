package com.artformgames.plugin.votepass.lobby.listener;

import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class UserListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        VotePassLobbyAPI.getUserManager().load(event.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            VotePassLobbyAPI.getUserManager().unload(event.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        LobbyUserData data = VotePassLobbyAPI.getUserManager().getNullable(e.getPlayer().getUniqueId());
        if (data == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            Optional.ofNullable(CommonMessages.LOAD_FAILED.parseToLine(e.getPlayer())).ifPresent(e::setKickMessage);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        VotePassLobbyAPI.getUserManager().unload(player.getUniqueId());
    }

}
