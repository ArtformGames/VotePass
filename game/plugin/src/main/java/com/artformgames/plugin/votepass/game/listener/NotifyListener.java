package com.artformgames.plugin.votepass.game.listener;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.user.GameUser;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import com.artformgames.plugin.votepass.game.vote.VoteManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NotifyListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UsersManager userManager = Main.getInstance().getUserManager();
        VoteManagerImpl voteManager = Main.getInstance().getVoteManager();

        Main.getInstance().getScheduler().runLater(60L, () -> {
            if (!player.isOnline()) return;

            GameUser user = userManager.getNullable(player.getUniqueId());
            if (user != null) {
                int requests = user.countUnvotedRequest();
                if (requests > 0) {
                    PluginMessages.VOTE.NOT_VOTED.send(player, requests);
                    PluginMessages.VOTE.SOUND.playTo(player);
                }
            }

            if (userManager.isAdmin(player)) {
                int requireIntervention = voteManager.countAdminRequests();
                if (requireIntervention > 0) {
                    PluginMessages.ADMIN.INTERVENTION.send(player, requireIntervention);
                    PluginMessages.ADMIN.SOUND.playTo(player);
                }
            }
        });
    }

}
