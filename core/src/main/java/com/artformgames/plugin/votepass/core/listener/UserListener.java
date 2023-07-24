package com.artformgames.plugin.votepass.core.listener;

import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.core.user.AbstractUserData;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UserListener<U extends AbstractUserData, M extends AbstractUserManager<? extends U>> implements Listener {

    protected final @NotNull M manager;

    public UserListener(@NotNull M manager) {
        this.manager = manager;
    }

    protected M getUserManager() {
        return this.manager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        try {
            UserKey key = getUserManager().upsertKey(event.getUniqueId(), event.getName());
            getUserManager().load(key, () -> true).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLoginMonitor(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            getUserManager().unload(event.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        U data = getUserManager().getNullable(e.getPlayer().getUniqueId());
        if (data == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            Optional.ofNullable(CommonMessages.LOAD_FAILED.parseToLine(e.getPlayer())).ifPresent(e::setKickMessage);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        getUserManager().unload(player.getUniqueId());
    }

}
