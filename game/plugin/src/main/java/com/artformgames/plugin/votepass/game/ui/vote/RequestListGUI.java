package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
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
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parseLine(player), 10, 34);
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
                .filter(v -> !v.isTimeout(PluginConfig.TIME.AUTO_CLOSE.getNotNull()))
                .filter(value -> !value.isVoted(getUser().getKey()))
                .forEachOrdered(value -> addItem(createIcon(value)));
    }

    protected GUIItem createIcon(@NotNull RequestInformation request) {
        RequestIconInfo iconInfo = RequestIconInfo.of(request);
        return new GUIItem(iconInfo.prepareIcon()
                .insert("click-lore", CONFIG.ADDITIONAL_LORE.CLICK)
                .get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                VoteHandleGUI.open(player, request, iconInfo);
            }
        };
    }

    public interface CONFIG extends Configuration {

        ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lAll Requests")
                .build();

        interface ADDITIONAL_LORE extends Configuration {

            ConfiguredMessage<String> CLICK = ConfiguredMessage.asString().defaults(
                    "&a ▶ Click &8|&f View details"
            ).build();

        }

    }
}
