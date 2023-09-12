package com.artformgames.plugin.votepass.game.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.ui.vote.VoteHandleGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommentListener implements Listener {

    protected static final Map<UUID, PendingVote> commenting = new HashMap<>();

    public static void startComment(@NotNull Player player, @NotNull PendingVote vote) {
        commenting.put(player.getUniqueId(), vote);
    }

    public static void cancelComment(@NotNull Player player) {
        commenting.remove(player.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        PendingVote vote = commenting.remove(player.getUniqueId());
        if (vote == null) return;

        event.setCancelled(true);
        String content = ColorParser.clear(event.getMessage());

        if (content.isEmpty() || content.isBlank() || content.equalsIgnoreCase("cancel")) {
            handle(player, vote);
            return;
        }

        int max = PluginConfig.COMMENT.MAX.getNotNull();
        if (content.length() > PluginConfig.COMMENT.MAX.getNotNull()) {
            PluginMessages.COMMENT.TOO_LONG.send(player, max);
            startComment(player, vote); // Retry
            return;
        }

        vote.setComments(content);
        handle(player, vote);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cancelComment(event.getPlayer());
    }

    public void handle(Player player, PendingVote vote) {
        Main.getInstance().getScheduler().run(() -> VoteHandleGUI.open(player, vote.getRequest(), null));
    }


}
