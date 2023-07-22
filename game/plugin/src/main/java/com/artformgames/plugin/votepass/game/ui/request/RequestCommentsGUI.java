package com.artformgames.plugin.votepass.game.ui.request;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RequestCommentsGUI extends AutoPagedGUI {

    private final Player player;
    private final @NotNull RequestInformation request;
    private final @NotNull RequestIconInfo iconInfo;
    private final @Nullable GUI backGUI;


    protected RequestCommentsGUI(Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo, @Nullable GUI backGUI) {
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parse(player, request.getID(), request.getUserDisplayName()), 19, 25);
        this.player = player;
        this.request = request;
        this.backGUI = backGUI;
        this.iconInfo = Optional.ofNullable(iconInfo).orElse(RequestIconInfo.of(request));
        GUIUtils.loadPageIcon(this, player, 18, 26);
        initItems();
    }

    public void initItems() {

        setItem(0, new GUIItem(CONFIG.ITEMS.INFO.get(player,
                iconInfo.displayName(), iconInfo.uuid(), iconInfo.id(), iconInfo.words(),
                iconInfo.createTime(), iconInfo.expireTime(),
                iconInfo.pros(), iconInfo.prosPercent(),
                iconInfo.cons(), iconInfo.consPercent(),
                iconInfo.abs(), iconInfo.absPercent(),
                iconInfo.total()
        )) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
                if (backGUI != null) backGUI.openGUI(player); // 返回上一页
            }
        });

        for (VoteInformation vote : request.getVotes()) {
            if (vote.comment() == null || vote.comment().isBlank()) continue;

            if (vote.isApproved()) {
                addItem(new GUIItem(CONFIG.ITEMS.APPROVED.get(
                        player, vote.voter().getDisplayName(), vote.comment(), vote.getTimeString())
                ));
            } else {
                addItem(new GUIItem(CONFIG.ITEMS.REJECTED.get(
                        player, vote.voter().getDisplayName(), vote.comment(), vote.getTimeString())
                ));
            }
        }

    }

    public static void open(Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo, @Nullable GUI backGUI) {
        player.closeInventory();
        new RequestCommentsGUI(player, request, iconInfo, backGUI).openGUI(player);
    }

    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&e&lComments &7| Request #&f%(id)")
                .params("id", "username")
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
                            " &a ▶ Click &8|&f Return to the request details page"
                    ).params("name", "uuid",
                            "request_id", "request_words",
                            "create_time", "close_time",
                            "pros_amount", "pros_ratio",
                            "cons_amount", "cons_ratio",
                            "abstains_amount", "abstains_ratio",
                            "votes_amount"
                    ).build();

            public static final ConfiguredItem APPROVED = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&7Comment from &f%(voter)")
                    .defaultLore(
                            " ",
                            "&7&oThis player approved this request",
                            "&7&oat &f&o%(time)",
                            " ",
                            "&f%(content)",
                            " "
                    ).params("voter", "content", "time").build();

            public static final ConfiguredItem REJECTED = ConfiguredItem.create()
                    .defaultType(Material.RED_STAINED_GLASS_PANE)
                    .defaultName("&7Comment from &f%(voter)")
                    .defaultLore(
                            " ",
                            "&7&oThis player rejected this request",
                            "&7&oat &f&o%(time)",
                            " ",
                            "&f%(content)",
                            " "
                    ).params("voter", "content", "time").build();

        }
    }
}
