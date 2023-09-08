package com.artformgames.plugin.votepass.game.ui.request;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.game.listener.CommentListener;
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

        setItem(0, new GUIItem(iconInfo.prepareIcon()
                .insertLore("click-lore", CONFIG.ADDITIONAL_LORE.CLICK)
                .get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
                if (backGUI != null) backGUI.openGUI(player); // 返回上一页
            }
        });

        for (VoteInformation vote : request.getVotes()) {
            if (vote.comment() == null || vote.comment().isBlank()) continue;

            if (vote.isApproved()) {
                addItem(new GUIItem(CONFIG.ITEMS.APPROVED
                        .prepare(vote.voter().getDisplayName(), vote.getTimeString())
                        .insertLore("comment", CommentListener.getCommentLore(vote.comment()))
                        .get(player)
                ));
            } else {
                addItem(new GUIItem(CONFIG.ITEMS.REJECTED
                        .prepare(vote.voter().getDisplayName(), vote.getTimeString())
                        .insertLore("comment", CommentListener.getCommentLore(vote.comment()))
                        .get(player)
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

        public static final class ADDITIONAL_LORE extends ConfigurationRoot {

            public static final ConfiguredMessageList<String> CLICK = ConfiguredMessageList.asStrings().defaults(
                    " &a ▶ Click &8|&f Return to the request details page"
            ).build();

        }

        public static final class ITEMS extends ConfigurationRoot {

            public static final ConfiguredItem APPROVED = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&7Comment from &f%(voter)")
                    .defaultLore(
                            " ",
                            "&7&oThis player approved this request",
                            "&7&oat &f&o%(time)",
                            " ",
                            "#comment#",
                            " "
                    ).params("voter", "time").build();

            public static final ConfiguredItem REJECTED = ConfiguredItem.create()
                    .defaultType(Material.RED_STAINED_GLASS_PANE)
                    .defaultName("&7Comment from &f%(voter)")
                    .defaultLore(
                            " ",
                            "&7&oThis player rejected this request",
                            "&7&oat &f&o%(time)",
                            " ",
                            "#comment#",
                            " "
                    ).params("voter", "time").build();

        }
    }
}
