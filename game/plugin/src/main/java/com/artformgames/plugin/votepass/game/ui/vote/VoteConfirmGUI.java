package com.artformgames.plugin.votepass.game.ui.vote;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import com.artformgames.plugin.votepass.game.user.GameUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class VoteConfirmGUI extends GUI {

    public static void open(@NotNull Player player, @NotNull RequestInformation request,
                            @NotNull VoteDecision decision, @Nullable RequestIconInfo iconInfo) {
        player.closeInventory();
        new VoteConfirmGUI(player, request, decision, iconInfo).openGUI(player);
    }

    private final @NotNull Player player;
    private final @NotNull RequestInformation request;
    private final @NotNull RequestIconInfo iconInfo;

    private final @NotNull VoteDecision decision;

    public VoteConfirmGUI(@NotNull Player player, @NotNull RequestInformation request, @NotNull VoteDecision decision, @Nullable RequestIconInfo iconInfo) {
        super(GUIType.FIVE_BY_NINE, CONFIG.TITLE.parse(player, request.getID(), request.getUserDisplayName()));
        this.player = player;
        this.request = request;
        this.iconInfo = Optional.ofNullable(iconInfo).orElse(RequestIconInfo.of(request));
        this.decision = decision;

        loadInfo();
        loadButtons();
    }

    public GameUser getVoteData() {
        return Main.getInstance().getUserManager().get(player.getUniqueId());
    }

    public PendingVote getPendingVote() {
        PendingVote vote = getVoteData().getPendingVote();
        if (vote == null || !vote.getRequest().equals(this.request)) {
            return getVoteData().createPendingRequest(this.request);
        } else {
            return vote;
        }
    }

    public void loadInfo() {
        setItem(12, new GUIItem(iconInfo.prepareIcon().get(player)));

        switch (decision) {
            case REJECT ->
                    setItem(14, new GUIItem(CONFIG.ITEMS.REJECTED.get(player, request.getID(), request.getUserDisplayName())));
            case ABSTAIN ->
                    setItem(14, new GUIItem(CONFIG.ITEMS.ABSTAINED.get(player, request.getID(), request.getUserDisplayName())));
            case APPROVE ->
                    setItem(14, new GUIItem(CONFIG.ITEMS.APPROVED.get(player, request.getID(), request.getUserDisplayName())));
        }

        if (getPendingVote().getComments() == null || getPendingVote().getComments().isBlank()) {
            setItem(40, new GUIItem(CONFIG.ITEMS.NOT_COMMENTED.get(player)));
        } else {
            setItem(40, new GUIItem(CONFIG.ITEMS.COMMENTED.get(player, getPendingVote().getComments())));
        }

    }

    public void loadButtons() {

        setItem(new GUIItem(CONFIG.ITEMS.CONFIRM.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();

                PendingVote pendingVote = getPendingVote();

                switch (decision) {
                    case APPROVE -> {
                        PluginMessages.VOTE.APPROVED.send(player, request.getID(), request.getUserDisplayName());
                        PluginConfig.SOUNDS.APPROVED.playTo(player);
                    }
                    case REJECT -> {
                        PluginMessages.VOTE.REJECTED.send(player, request.getID(), request.getUserDisplayName());
                        PluginConfig.SOUNDS.REJECT.playTo(player);
                    }
                    case ABSTAIN -> {
                        PluginMessages.VOTE.ABSTAINED.send(player, request.getID(), request.getUserDisplayName());
                        PluginConfig.SOUNDS.ABSTAIN.playTo(player);
                    }
                }

                Main.getInstance().getVoteManager().submitVote(getVoteData(), pendingVote);
                getVoteData().removePendingVote(); // 移除本次投票信息
            }
        }, 36, 37, 38, 39);

        setItem(new GUIItem(CONFIG.ITEMS.CANCEL.get(player, request.getID(), request.getUserDisplayName())) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();
                VoteHandleGUI.open(player, request, iconInfo);
            }
        }, 41, 42, 43, 44);
    }


    public static final class CONFIG extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lConfirm Vote | &7#%(id)")
                .params("id", "username")
                .build();

        public static final class ITEMS extends ConfigurationRoot {

            public static final ConfiguredItem APPROVED = ConfiguredItem.create()
                    .defaultType(Material.EMERALD)
                    .defaultName("&7You decision is &a&lApproved")
                    .defaultLore(
                            " ",
                            "&fYou will vote '&a&lApproved&f'",
                            "&fto player &e%(player) &f's request",
                            " "
                    ).params("id", "player").build();

            public static final ConfiguredItem ABSTAINED = ConfiguredItem.create()
                    .defaultType(Material.COAL)
                    .defaultName("&7You decision is &e&lAbstained")
                    .defaultLore(
                            " ",
                            "&fYou have given up your right to vote this time.",
                            " "
                    ).params("id", "player").build();

            public static final ConfiguredItem REJECTED = ConfiguredItem.create()
                    .defaultType(Material.REDSTONE)
                    .defaultName("&7You decision is &c&lRejected")
                    .defaultLore(
                            " ",
                            "&fYou will vote '&c&lRejected&f'",
                            "&fto player &e%(player) &f's request",
                            " "
                    ).params("id", "player").build();

            public static final ConfiguredItem CONFIRM = ConfiguredItem.create()
                    .defaultType(Material.GREEN_STAINED_GLASS_PANE)
                    .defaultName("&a&lConfirm your vote")
                    .build();

            public static final ConfiguredItem CANCEL = ConfiguredItem.create()
                    .defaultType(Material.RED_STAINED_GLASS_PANE)
                    .defaultName("&7Consider later")
                    .build();

            public static final ConfiguredItem NOT_COMMENTED = ConfiguredItem.create()
                    .defaultType(Material.PAPER)
                    .defaultName("&f&lPersonal comments")
                    .defaultLore(
                            " ",
                            "&7&oYou have not commented for this request,",
                            "&7&oGood comment can help others to make a decision."
                    )
                    .build();

            public static final ConfiguredItem COMMENTED = ConfiguredItem.create()
                    .defaultType(Material.PAPER)
                    .defaultName("&f&lPersonal comments")
                    .defaultLore(
                            " ",
                            "&7&oYou commented:",
                            "&f %(comment)",
                            " "
                    ).params("comment")
                    .build();
        }
    }
}