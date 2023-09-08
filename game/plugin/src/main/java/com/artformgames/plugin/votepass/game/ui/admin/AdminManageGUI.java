package com.artformgames.plugin.votepass.game.ui.admin;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class AdminManageGUI extends AutoPagedGUI {

    public static void open(Player player) {
        new AdminManageGUI(player).openGUI(player);
    }

    private final Player player;
    private final GameUser user;

    public AdminManageGUI(Player player) {
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
                .filter(value -> !PluginConfig.SERVER.MANAGE_RESTRICT.getNotNull()
                        || value.needIntervention(CommonConfig.TIME.ADMIN_INTERVENTION.get())
                ).forEachOrdered(value -> addItem(createIcon(value)));
    }

    protected GUIItem createIcon(@NotNull RequestInformation request) {
        RequestIconInfo iconInfo = RequestIconInfo.of(request);
        return new GUIItem(iconInfo.prepareIcon()
                .insertLore("click-lore", CONFIG.ADDITIONAL_LORE.CLICK)
                .get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                AdminHandleGUI.open(player, request, iconInfo);
            }
        };
    }

    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lManage Requests")
                .build();

        public static final class ADDITIONAL_LORE extends ConfigurationRoot {

            public static final ConfiguredMessageList<String> CLICK = ConfiguredMessageList.asStrings().defaults(
                    "&a ▶ Click &8|&f Manage this request"
            ).build();

        }

    }
}
