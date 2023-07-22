package com.artformgames.plugin.votepass.game.ui.vote;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.entity.Player;

import java.util.List;

public class VoteCommentGUI {

    private VoteCommentGUI() {
    }

    public static void open(Player player, RequestIconInfo info) {
        player.closeInventory();
        Main.getSignAPI().createGUI(null, (p, lines) -> {
            if (!player.isOnline()) return;

            GameUser user = Main.getInstance().getUserManager().get(player.getUniqueId());
            PendingVote vote = user.getPendingVote();
            if (vote == null) return;

            vote.setComments(List.of(lines));

            Main.getInstance().getScheduler().runLater(2L, () -> VoteHandleGUI.open(player, vote.getRequest(), info));
        }).open(player);
    }

}
