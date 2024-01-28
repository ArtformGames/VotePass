package com.artformgames.plugin.votepass.lobby.ui;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.value.item.PreparedItem;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResubmitGUI extends AutoPagedGUI {

    public static void open(@NotNull Player player, @NotNull LobbyUserData data,
                            @NotNull ServerSettings server, @NotNull RequestInformation lastFailed) {
        ResubmitGUI questionsGUI = new ResubmitGUI(player, data, server, lastFailed);
        questionsGUI.openGUI(player);
    }

    protected final @NotNull Player player;
    protected final @NotNull LobbyUserData data;
    protected final @NotNull ServerSettings server;
    protected final @NotNull RequestInformation request;

    public ResubmitGUI(@NotNull Player player, @NotNull LobbyUserData data,
                       @NotNull ServerSettings server, @NotNull RequestInformation lastFailed) {
        super(GUIType.SIX_BY_NINE, Objects.requireNonNull(PluginConfig.RESUBMIT.TITLE.parse(player, lastFailed.getServer())), 10, 34);
        this.player = player;
        this.data = data;
        this.server = server;
        this.request = lastFailed;
        initItems();

        setPreviousPageSlot(18);
        setNextPageSlot(26);
        setPreviousPageUI(CommonConfig.PAGE_ITEMS.PREVIOUS_PAGE.get(player));
        setNextPageUI(CommonConfig.PAGE_ITEMS.NEXT_PAGE.get(player));
    }


    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull LobbyUserData getData() {
        return data;
    }

    public @NotNull RequestInformation getRequest() {
        return request;
    }

    public void initItems() {
        setItem(4, new GUIItem(PluginConfig.RESUBMIT.ITEMS.INFO.prepare(
                getRequest().getID(),
                getRequest().count(VoteDecision.APPROVE),
                getRequest().count(VoteDecision.REJECT),
                getRequest().count(VoteDecision.ABSTAIN)
        ).get(player)));

        getRequest().getContents().forEach((i, a) -> {
            PreparedItem item = PluginConfig.RESUBMIT.ITEMS.ANSWERS
                    .prepare(i, a.question())
                    .insertLore("answers", ColorParser.parse(a.answers()));
            addItem(new GUIItem(item.get(player)));
        });

        setItem(createCancelItem(), 50, 51, 52);
        setItem(createConfirmItem(), 46, 47, 48);
    }


    protected GUIItem createConfirmItem() {
        return new GUIItem(PluginConfig.RESUBMIT.ITEMS.CONFIRM.get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();

                PendingRequest pending = new PendingRequest(server);
                getRequest().getContents().forEach((i, a) -> pending.applyAnswer(i, a.answers()));

                RequestingGUI.open(player, data, pending);
            }
        };
    }

    protected GUIItem createCancelItem() {
        return new GUIItem(PluginConfig.RESUBMIT.ITEMS.CANCEL.get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                getPlayer().closeInventory();
                RequestingGUI.open(player, data, new PendingRequest(server));
            }
        };
    }

}
