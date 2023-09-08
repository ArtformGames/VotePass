package com.artformgames.plugin.votepass.game.listener;

import cc.carm.lib.easyplugin.utils.ColorParser;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class CommentListener implements Listener {

    protected static final Map<UUID, BiConsumer<Player, String>> commenting = new HashMap<>();

    public static void startComment(@NotNull Player player, @NotNull BiConsumer<Player, String> callback) {
        commenting.put(player.getUniqueId(), callback);
    }

    public static void cancelComment(@NotNull Player player) {
        commenting.remove(player.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        BiConsumer<Player, String> callback = commenting.remove(player.getUniqueId());
        if (callback == null) return;

        event.setCancelled(true);
        String content = ColorParser.clear(event.getMessage());

        if (content.isEmpty() || content.isBlank() || content.equalsIgnoreCase("cancel")) {
            return;
        }

        int max = PluginConfig.COMMENT.MAX.getNotNull();
        if (content.length() > PluginConfig.COMMENT.MAX.getNotNull()) {
            PluginMessages.COMMENT.TOO_LONG.send(player, max);
            startComment(player, callback); // Retry
            return;
        }

        callback.accept(player, content);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cancelComment(event.getPlayer());
    }

    public static List<String> getCommentLore(String content) {
        List<String> lore = new ArrayList<>();
        if (content == null || content.isBlank()) return lore;

        int line = PluginConfig.COMMENT.LINE.getNotNull();
        String prefix = PluginConfig.COMMENT.PREFIX.getNotNull();

        int length = content.length();
        int lines = length / line + (length % line == 0 ? 0 : 1);
        for (int i = 0; i < lines; i++) {
            int start = i * line;
            int end = Math.min((i + 1) * line, length);
            lore.add(prefix + content.substring(start, end));
        }

        return lore;
    }

    public static List<String> getCommentLore(PendingVote vote) {
        return getCommentLore(vote.getComments());
    }

}
