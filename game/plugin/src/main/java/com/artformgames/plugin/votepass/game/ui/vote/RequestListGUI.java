package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Optional;

public class RequestListGUI extends AutoPagedGUI {

    public static void open(Player player) {
        new RequestListGUI(player).openGUI(player);
    }

    private final Player player;
    private final GameUser user;

    public RequestListGUI(Player player) {
        super(GUIType.SIX_BY_NINE, PluginConfig.GUIS.LIST.TITLE.parse(player), 10, 34);
        this.player = player;
        this.user = Main.getInstance().getUserManager().get(player.getUniqueId());

        setPreviousPageSlot(18);
        setNextPageSlot(26);
        setNextPageUI(CommonConfig.PAGE_ITEMS.NEXT_PAGE.get(player));
        setPreviousPageUI(CommonConfig.PAGE_ITEMS.PREVIOUS_PAGE.get(player));

        initItems();
    }


    public Player getPlayer() {
        return player;
    }

    public GameUser getUser() {
        return this.user;
    }

    public void initItems() {
        Main.getInstance().getVoteManager().getRequests().values().stream()
                .filter(value -> !value.isVoted(getUser().getKey()))
                .forEach(value -> addItem(createIcon(value)));
    }

    protected GUIItem createIcon(@NotNull RequestInformation info) {

        int total = info.count(null);
        int pros = info.count(VoteDecision.APPROVE);
        int abs = info.count(VoteDecision.ABSTAIN);
        int cons = total - pros - abs;

        int words = info.countAnswerWords();

        return new GUIItem(PluginConfig.GUIS.LIST.ITEMS.INFO.get(player,
                Optional.ofNullable(info.getUser().name()).orElse("?"), info.getUser().uuid(),
                info.getID(), info.countAnswerWords(),
                info.getCreateTimeString(),
                info.getExpireTimeString(CommonConfig.TIME.AUTO_CLOSE.getNotNull()),
                pros, getPercent(pros, total),
                cons, getPercent(cons, total),
                abs, getPercent(abs, total),
                total
        )) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (type.isLeftClick() || words >= 5000) {
                    VoteHandleGUI.open(player, info);
                } else if (type.isRightClick()) {
                    player.closeInventory();
                    if (!QuickReviewGUI.open(player, info)) {
                        VoteHandleGUI.open(player, info);
                    }
                } else {
                    player.closeInventory();
                }
            }
        };
    }


    protected static String getPercent(int x, int y) {
        if (x == 0 || y == 0) return "0.00";

        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(d1 / d2);
    }

}
