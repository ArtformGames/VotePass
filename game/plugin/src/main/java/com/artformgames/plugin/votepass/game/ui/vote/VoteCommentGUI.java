package com.artformgames.plugin.votepass.game.ui.vote;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class VoteCommentGUI {

    private VoteCommentGUI() {
    }

    public static void open(Player player, @NotNull PendingVote vote, RequestIconInfo info) {
        player.closeInventory();
        Main.getSignAPI().createGUI(null, (p, lines) -> {
            if (!player.isOnline()) return;

            vote.setComments(Arrays.asList(lines));
            Main.getInstance().getScheduler().runLater(2L, () -> VoteHandleGUI.open(player, vote.getRequest(), info));
        }).open(player);
    }

}
