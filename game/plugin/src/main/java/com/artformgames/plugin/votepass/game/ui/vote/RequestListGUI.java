package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class RequestListGUI extends AutoPagedGUI {


    public static void open(Player player) {
        new RequestListGUI(player).openGUI(player);
    }

    private final Player player;
    private final GameUser user;

    public RequestListGUI(Player player) {
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parse(player), 10, 34);
        this.player = player;
        this.user = Main.getInstance().getUserManager().get(player.getUniqueId());

        GUIUtils.loadPageIcon(this, player, 18, 26);
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
                .forEachOrdered(value -> addItem(createIcon(value)));
    }

    protected GUIItem createIcon(@NotNull RequestInformation request) {
        RequestIconInfo iconInfo = RequestIconInfo.of(request);
        return new GUIItem(iconInfo.prepareIcon()
                .insertLore("click-lore", CONFIG.ADDITIONAL_LORE.CLICK)
                .get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (type.isLeftClick() || iconInfo.words() >= 5000) {
                    VoteHandleGUI.open(player, request, iconInfo);
                } else if (type.isRightClick()) {
                    player.closeInventory();
                    if (!QuickReviewGUI.open(player, request)) {
                        VoteHandleGUI.open(player, request, iconInfo);
                    }
                } else {
                    player.closeInventory();
                }
            }
        };
    }

    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lAll Requests")
                .build();

        public static final class ADDITIONAL_LORE extends ConfigurationRoot {

            public static final ConfiguredMessageList<String> CLICK = ConfiguredMessageList.asStrings().defaults(
                    "&a ▶ Left  click &8|&f view details",
                    "&a ▶ Right click &8|&f quick review"
            ).build();

        }

    }
}
