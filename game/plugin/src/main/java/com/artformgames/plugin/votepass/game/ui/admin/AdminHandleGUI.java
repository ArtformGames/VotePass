package com.artformgames.plugin.votepass.game.ui.admin;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.game.Main;
import org.bukkit.entity.Player;

public class AdminHandleGUI {

    public static void open(Player player, RequestInformation request) {
        player.closeInventory();
        if (!Main.getInstance().getUserManager().isAdmin(player)) return;

    }

}
