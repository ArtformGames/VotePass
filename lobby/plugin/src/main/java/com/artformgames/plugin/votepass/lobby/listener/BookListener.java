package com.artformgames.plugin.votepass.lobby.listener;

import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.ui.RequestingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

public class BookListener implements Listener {

    @EventHandler
    public void onClose(PlayerEditBookEvent event) {
        LobbyUserData data = VotePassLobbyAPI.getUserManager().getNullable(event.getPlayer().getUniqueId());
        if (data == null) return;

        PendingRequest request = data.getPendingRequest();
        if (request == null) return;
        Player player = event.getPlayer();

        request.applyAnswer(event.getNewBookMeta().getPages());
        Main.getInstance().getScheduler().runLater(5L, () -> player.getInventory().setItemInMainHand(null));

        RequestingGUI.open(player, data, request);
    }

}
