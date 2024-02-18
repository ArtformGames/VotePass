package com.artformgames.plugin.votepass.lobby.listener;

import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class FeedbackListener implements Listener {

    @EventHandler
    public void feedbackPlayer(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        Main.getInstance().getScheduler().runLater(50L, () -> {
            if (!player.isOnline()) return;
            LobbyUserData data = VotePassLobbyAPI.getUserManager().get(player.getUniqueId());

            Set<Integer> removal = data.listRequests().values().stream()
                    .filter(value -> handleRequest(player, value))
                    .map(RequestInformation::getID)
                    .collect(Collectors.toSet());
            removal.forEach(data::removeRequest);
        });

    }

    private boolean handleRequest(Player player, RequestInformation request) {
        return switch (request.getResult()) {
            case REJECTED -> feedbackRequest(player, PluginMessages.FEEDBACK.REJECTED, request);
            case APPROVED -> feedbackRequest(player, PluginMessages.FEEDBACK.APPROVED, request);
            case EXPIRED -> feedbackRequest(player, PluginMessages.FEEDBACK.EXPIRED, request);
            default -> false;
        };
    }

    private boolean feedbackRequest(Player player, ConfiguredMessageList<?> message, RequestInformation request) {
        Main.debugging("Handling " + request.getResult().name() + " -> #" + request.getID());
        ServerSettings configuration = VotePassLobbyAPI.getServersManager().getSettings(request.getServer());
        if (configuration != null) {
            message.send(player, request.getID(), configuration.name());
            PluginMessages.FEEDBACK.SOUND.playTo(player);
        }

        request.setFeedback(true);
        VotePassLobbyAPI.getRequestManager().update(request);
        return true;
    }

}
