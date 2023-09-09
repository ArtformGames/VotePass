package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.listener.CommentListener;
import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.ui.request.RequestAnswerGUI;
import com.artformgames.plugin.votepass.game.ui.request.RequestCommentsGUI;
import com.artformgames.plugin.votepass.game.user.GameUser;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VoteHandleGUI extends AutoPagedGUI {

    public static void open(@NotNull Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo) {
        player.closeInventory();
        new VoteHandleGUI(player, request, iconInfo).openGUI(player);
    }

    private final @NotNull Player player;
    private final @NotNull RequestInformation request;
    private final @NotNull RequestIconInfo iconInfo;

    public VoteHandleGUI(@NotNull Player player, @NotNull RequestInformation request, @Nullable RequestIconInfo iconInfo) {
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parse(player, request.getID(), request.getUsername()), 19, 25);
        this.player = player;
        this.request = request;
        this.iconInfo = Optional.ofNullable(iconInfo).orElse(RequestIconInfo.of(request));
        GUIUtils.loadPageIcon(this, player, 18, 26);
        loadButtons();
        loadAnswers();
    }

    public GameUser getVoteData() {
        return Main.getInstance().getUserManager().get(player.getUniqueId());
    }

    public PendingVote getPendingVote() {
        PendingVote vote = getVoteData().getPendingVote();
        if (vote == null || vote.getRequest().getID() != this.request.getID()) {
            return getVoteData().createPendingRequest(this.request);
        } else {
            return vote;
        }
    }

    public void loadButtons() {

        setItem(4, new GUIItem(iconInfo.prepareIcon().get(player)));

        setItem(new GUIItem(CONFIG.ITEMS.APPROVE.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                getPendingVote().setDecision(VoteDecision.APPROVE);
                VoteConfirmGUI.open(player, request, VoteDecision.APPROVE, iconInfo);
            }
        }, 36, 37, 38, 39);

        setItem(new GUIItem(CONFIG.ITEMS.REJECT.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                getPendingVote().setDecision(VoteDecision.REJECT);
                VoteConfirmGUI.open(player, request, VoteDecision.REJECT, iconInfo);
            }
        }, 41, 42, 43);

        setItem(new GUIItem(CONFIG.ITEMS.ABSTAIN.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                getPendingVote().setDecision(VoteDecision.ABSTAIN);
                VoteConfirmGUI.open(player, request, VoteDecision.ABSTAIN, iconInfo);
            }
        }, 44);

        ItemStack commentIcon;

        if (getPendingVote().getComments() == null || getPendingVote().getComments().isBlank()) {
            commentIcon = CONFIG.ITEMS.NOT_COMMENTED.prepare(request.countCommentedVotes()).get(player);
        } else {
            commentIcon = CONFIG.ITEMS.COMMENTED.prepare(request.countCommentedVotes())
                    .insertLore("comment", CommentListener.getCommentLore(getPendingVote()))
                    .get(player);
        }

        setItem(40, new GUIItem(commentIcon) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                if (type == ClickType.LEFT || type == ClickType.SHIFT_LEFT) {
                    RequestCommentsGUI.open(player, request, iconInfo, VoteHandleGUI.this);
                } else if (type == ClickType.RIGHT || type == ClickType.SHIFT_RIGHT) {
                    player.closeInventory();
                    PluginMessages.COMMENT.START.send(player, request.getID(), request.getUserDisplayName());
                    CommentListener.startComment(player, getPendingVote());
                }
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

            public static final ConfiguredItem APPROVE = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&a&lApprove &fthe request &8#&f%(id)")
                    .params("id", "username")
                    .build();

            public static final ConfiguredItem ABSTAIN = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&e&lAbstain &ffrom voting")
                    .params("id", "username")
                    .build();


            public static final ConfiguredItem REJECT = ConfiguredItem.create()
                    .defaultType(Material.RED_STAINED_GLASS_PANE)
                    .defaultName("&c&lReject &fthe request &8#&f%(id)")
                    .params("id", "username")
                    .build();


            public static final ConfiguredItem NOT_COMMENTED = ConfiguredItem.create()
                    .defaultType(Material.PAPER)
                    .defaultName("&f&lPersonal comments")
                    .defaultLore(
                            " ",
                            "&fThis request now has &E%(amount) &fcomments.",
                            " ",
                            "&7&oYou have not commented for this request,",
                            "&7&oGood comment can help others to make a decision.",
                            " ",
                            "&a ▶ Left  click &8|&f View other's comments",
                            "&a ▶ Right click &8|&f Write your comment"
                    )
                    .params("amount")
                    .build();

            public static final ConfiguredItem COMMENTED = ConfiguredItem.create()
                    .defaultType(Material.PAPER)
                    .defaultName("&f&lPersonal comments")
                    .defaultLore(
                            " ",
                            "&fThis request now has &E%(amount) &fcomments.",
                            " ",
                            "&7&oYou commented:",
                            "#comment#",
                            " ",
                            "&a ▶ Left  click &8|&f View other's comments",
                            "&a ▶ Right click &8|&f Edit your comment"
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
                            "[Click here](hover=Click to return to the details page and continue processing related answers. run_command=/votepass handle %(id)) to return to the request details page."
                    ).params("id")
                    .build();

        }
    }
}
