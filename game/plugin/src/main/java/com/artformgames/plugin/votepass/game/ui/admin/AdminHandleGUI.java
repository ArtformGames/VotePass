package com.artformgames.plugin.votepass.game.ui.admin;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.ui.request.RequestAnswerGUI;
import com.artformgames.plugin.votepass.game.ui.request.RequestCommentsGUI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AdminHandleGUI extends AutoPagedGUI {

    public static void open(@NotNull Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo) {
        player.closeInventory();
        new AdminHandleGUI(player, request, iconInfo).openGUI(player);
    }

    private final @NotNull Player player;
    private final @NotNull RequestInformation request;
    private final @NotNull RequestIconInfo iconInfo;

    public AdminHandleGUI(@NotNull Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo) {
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parse(player, request.getID(), request.getUsername()), 19, 25);
        this.player = player;
        this.request = request;
        this.iconInfo = Optional.ofNullable(iconInfo).orElse(RequestIconInfo.of(request));
        GUIUtils.loadPageIcon(this, player, 18, 26);

        loadButtons();
        loadAnswers();
    }

    public void loadButtons() {

        setItem(4, new GUIItem(CONFIG.ITEMS.INFO.get(player,
                iconInfo.displayName(), iconInfo.uuid(), iconInfo.id(), iconInfo.words(),
                iconInfo.createTime(), iconInfo.expireTime(),
                iconInfo.pros(), iconInfo.prosPercent(),
                iconInfo.cons(), iconInfo.consPercent(),
                iconInfo.abs(), iconInfo.absPercent(),
                iconInfo.total()
        )));

        setItem(new GUIItem(CONFIG.ITEMS.APPROVE.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
                if (!player.hasPermission("votepass.admin")) return;
                Main.getInstance().getVoteManager().approve(request);
                PluginMessages.ADMIN.APPROVED.send(player, request.getID(), request.getUsername());
            }
        }, 36, 37, 38, 39);

        setItem(new GUIItem(CONFIG.ITEMS.REJECT.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
                if (!player.hasPermission("votepass.admin")) return;
                Main.getInstance().getVoteManager().reject(request);
                PluginMessages.ADMIN.REJECTED.send(player, request.getID(), request.getUsername());
            }
        }, 41, 42, 43, 44);

        setItem(40, new GUIItem(CONFIG.ITEMS.COMMENTED.get(player, request.countCommentedVotes())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                RequestCommentsGUI.open(player, request, iconInfo, AdminHandleGUI.this);
            }
        });

    }

    public void loadAnswers() {
        for (RequestAnswer value : request.getContents().values()) {
            addItem(new GUIItem(CONFIG.ITEMS.ANSWER.get(player, value.question(), value.countWords())) {
                @Override
                public void onClick(Player clicker, ClickType type) {
                    player.closeInventory();
                    PluginMessages.VOTE.VIEWING.send(player, request.getID(), request.getUserDisplayName(), value.question());
                    RequestAnswerGUI.open(player, request, value, CONFIG.BOOK.RETURN.parseToLine(player, request.getID()));
                }
            });
        }
    }

    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lHandle Request &7#%(id)")
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
                            "&f◮ &e&lAbstained&7: &7%(abstains_amount)&7/%(votes_amount) &8(%(abstains_ratio)%)"
                    ).params("name", "uuid",
                            "request_id", "request_words",
                            "create_time", "close_time",
                            "pros_amount", "pros_ratio",
                            "cons_amount", "cons_ratio",
                            "abstains_amount", "abstains_ratio",
                            "votes_amount"
                    ).build();

            public static final ConfiguredItem APPROVE = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&a&lApprove &fthe request &8#&f%(id)")
                    .params("id", "username")
                    .build();

            public static final ConfiguredItem REJECT = ConfiguredItem.create()
                    .defaultType(Material.RED_STAINED_GLASS_PANE)
                    .defaultName("&c&lReject &fthe request &8#&f%(id)")
                    .params("id", "username")
                    .build();

            public static final ConfiguredItem COMMENTED = ConfiguredItem.create()
                    .defaultType(Material.PAPER)
                    .defaultName("&f&lRequest comments")
                    .defaultLore(
                            " ",
                            "&fThis request now has &E%(amount) &fcomments.",
                            " ",
                            "&a ▶ Click &8|&f View other's comments"
                    )
                    .params("amount")
                    .build();

            public static final ConfiguredItem ANSWER = ConfiguredItem.create()
                    .defaultType(Material.BOOK)
                    .defaultName("&7Question: &f%(question)")
                    .defaultLore(
                            " ",
                            "&fThis answer contains &e%(words) &fwords.",
                            " ",
                            "&a ▶ Click &8|&f View answer contents"
                    )
                    .params("question", "words")
                    .build();

        }

        public static final class BOOK extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> RETURN = PluginMessages.list()
                    .defaults(
                            "All answers have been displayed.",
                            "[Click here](hover=Click to return to the details page and continue processing related answers. run_command=/votepass manage %(id)) to return to the request details page."
                    ).params("id")
                    .build();

        }
    }
}
