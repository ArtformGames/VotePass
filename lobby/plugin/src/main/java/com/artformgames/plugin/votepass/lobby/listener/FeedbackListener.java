package com.artformgames.plugin.votepass.lobby.listener;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.time.LocalDateTime;
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
        ServerSettings configuration = VotePassLobbyAPI.getServersManager().getSettings(request.getServer());
        switch (request.getResult()) {
            case REJECTED -> {
                handleDeniedRequest(player, configuration, request);
                return true;
            }
            case APPROVED -> {
                handleApprovedRequest(player, configuration, request);
                return true;
            }
            default -> {
                if (request.isTimeout(CommonConfig.TIME.AUTO_CLOSE.getNotNull())) {
                    handleExpiredRequest(player, configuration, request);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private void handleDeniedRequest(Player player, ServerSettings configuration, RequestInformation request) {
        Main.debugging("Handling denied #" + request.getID());
        if (configuration != null) {
            PluginMessages.FEEDBACK.REJECTED.send(player, request.getID(), configuration.name());
            PluginMessages.FEEDBACK.SOUND.playTo(player);
        }

        request.setFeedback(true);
        VotePassLobbyAPI.getRequestManager().update(request);
    }

    private void handleExpiredRequest(Player player, ServerSettings configuration, RequestInformation request) {
        Main.debugging("Handling expired #" + request.getID());
        if (configuration != null) {
            PluginMessages.FEEDBACK.EXPIRED.send(player, request.getID(), configuration.name());
            PluginMessages.FEEDBACK.SOUND.playTo(player);
        }
        
        request.setResult(RequestResult.REJECTED);
        request.setCloseTime(LocalDateTime.now());
        request.setFeedback(true);
        VotePassLobbyAPI.getRequestManager().update(request);
    }

    private void handleApprovedRequest(Player player, ServerSettings configuration, RequestInformation request) {
        Main.debugging("Handling approved #" + request.getID());
        if (configuration != null) {
            PluginMessages.FEEDBACK.APPROVED.send(player, request.getID(), configuration.name());
            PluginMessages.FEEDBACK.SOUND.playTo(player);
        }

        request.setFeedback(true);
        VotePassLobbyAPI.getRequestManager().update(request);
    }

}
