package com.artformgames.plugin.votepass.game.ui.admin;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.Material;
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
                .filter(value -> value.needIntervention(CommonConfig.TIME.ADMIN_INTERVENTION.get()))
                .forEachOrdered(value -> addItem(createIcon(value)));
    }

    protected GUIItem createIcon(@NotNull RequestInformation request) {
        RequestIconInfo iconInfo = RequestIconInfo.of(request);
        return new GUIItem(CONFIG.ITEMS.INFO.get(player,
                iconInfo.displayName(), iconInfo.uuid(), iconInfo.id(), iconInfo.words(),
                iconInfo.createTime(), iconInfo.expireTime(),
                iconInfo.pros(), iconInfo.prosPercent(),
                iconInfo.cons(), iconInfo.consPercent(),
                iconInfo.abs(), iconInfo.absPercent(),
                iconInfo.total()
        )) {
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

        public static final class ITEMS extends ConfigurationRoot {

            public static final ConfiguredItem INFO = ConfiguredItem.create()
                    .defaultType(Material.PLAYER_HEAD)
                    .defaultName("&7#%(request_id) &e&l%(name)")
                    .defaultLore(
                            " ",
                            "&7Request form &e&l%(name)",
                            "&7UUID: &e%(uuid)",
                            "&7",
                            "&7Contain words: &e%(request_words)",
                            "&7Submit time: &e%(create_time)",
                            "&7Close time: &e%(close_time)",
                            " ",
                            "&f✔ &a&lApproved&7: &a%(pros_amount)&7/%(votes_amount) &8(%(pros_ratio)%)",
                            "&f✘ &c&lRejected&7: &c%(cons_amount)&7/%(votes_amount) &8(%(cons_ratio)%)",
                            "&f◮ &e&lAbstained&7: &7%(abstains_amount)&7/%(votes_amount) &8(%(abstains_ratio)%)",
                            " ",
                            "&a ▶ Click &8|&f Manage this request"
                    ).params("name", "uuid",
                            "request_id", "request_words",
                            "create_time", "close_time",
                            "pros_amount", "pros_ratio",
                            "cons_amount", "cons_ratio",
                            "abstains_amount", "abstains_ratio",
                            "votes_amount"
                    ).build();
        }


    }
}
