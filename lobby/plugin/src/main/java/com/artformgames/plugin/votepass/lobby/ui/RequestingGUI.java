package com.artformgames.plugin.votepass.lobby.ui;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.item.PreparedItem;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerQuestion;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.event.RequestCreatedEvent;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RequestingGUI extends AutoPagedGUI {

    public static void open(Player player, LobbyUserData data, PendingRequest request) {
        RequestingGUI questionsGUI = new RequestingGUI(player, data, request);
        questionsGUI.openGUI(player);
    }

    @NotNull
    Player player;
    @NotNull
    LobbyUserData data;
    @NotNull
    PendingRequest pendingRequest;

    public RequestingGUI(@NotNull Player player, @NotNull LobbyUserData data, @NotNull PendingRequest pendingRequest) {
        super(
                GUIType.SIX_BY_NINE,
                Objects.requireNonNull(PluginConfig.ANSWERING.TITLE.parse(player, pendingRequest.getSettings().name())),
                10, 34
        );
        this.player = player;
        this.data = data;
        this.pendingRequest = pendingRequest;
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

    public @NotNull PendingRequest getPendingRequest() {
        return pendingRequest;
    }

    public ServerSettings getSettings() {
        return getPendingRequest().getSettings();
    }

    public void initItems() {
        for (Map.Entry<Integer, ServerQuestion> entry : getSettings().questions().entrySet()) {
            addItem(createQuestionItem(entry.getKey(), entry.getValue()));
        }

        setItem(createCancelItem(), 50, 51, 52);
        if (getSettings().questions().size() == getPendingRequest().getAnswers().size()) {
            setItem(createConfirmItem(), 46, 47, 48);
        } else {
            setItem(createPendingItem(), 46, 47, 48);
        }
    }

    protected GUIItem createQuestionItem(int id, ServerQuestion question) {
        boolean isAnswered = getPendingRequest().isAnswered(id);
        ConfiguredItem configuredItem = isAnswered ? PluginConfig.ANSWERING.ITEMS.FINISHED : PluginConfig.ANSWERING.ITEMS.REQUIRED;
        PreparedItem item = configuredItem.prepare(question.title());
        item.insertLore("description", ColorParser.parse(question.description()));

        return new GUIItem(item.get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();

                PendingRequest request = data.getPendingRequest();
                if (request == null) return;
                int slot = PluginConfig.ANSWERING.BOOK_ITEM.SLOT.getNotNull();

                PluginMessages.WRITING.send(player, slot, question.title());
                request.setEditingQuestion(id);
                player.getInventory().setHeldItemSlot(slot);

                List<String> existsAnswer = Optional.ofNullable(request.getAnswers().get(id))
                        .map(RequestAnswer::answers).orElse(null);

                player.getInventory().setItem(slot, generateAnswerBook(existsAnswer));
            }
        };
    }

    protected GUIItem createConfirmItem() {
        return new GUIItem(PluginConfig.ANSWERING.ITEMS.CONFIRM.get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                player.closeInventory();

                RequestInformation existRequest = getData().getServerRequest(getSettings().id());
                if (existRequest != null) {
                    PluginMessages.PENDING.send(player, existRequest.getID(), getSettings().name());
                    return;
                }

                if (getData().isPassed(getSettings().id())) {
                    PluginMessages.WHITELISTED.send(player, getSettings().name());
                    return;
                }

                VotePassLobbyAPI.getRequestManager()
                        .commit(getData().getKey(), getPendingRequest())
                        .thenCompose(request -> {
                            if (request == null) {
                                PluginMessages.ERROR.send(player, getSettings().name());
                                return CompletableFuture.completedFuture(null);
                            }

                            getData().addRequest(request);
                            getData().removePendingRequest();
                            PluginMessages.POSTED.send(player, request.getID(), getSettings().name());
                            return Main.getInstance().callAsync(new RequestCreatedEvent(request));
                        });
            }
        };
    }

    protected GUIItem createCancelItem() {
        return new GUIItem(PluginConfig.ANSWERING.ITEMS.CANCEL.get(player)) {
            @Override
            public void onClick(Player clicker, ClickType type) {
                getPlayer().closeInventory();

                PluginMessages.CANCELLED.send(player, getSettings().name());
                getData().removePendingRequest();
            }
        };
    }

    protected GUIItem createPendingItem() {
        return new GUIItem(PluginConfig.ANSWERING.ITEMS.PENDING.get(player));
    }

    public ItemStack generateAnswerBook(@Nullable List<String> existingPages) {
        ItemStackFactory factory = new ItemStackFactory(Material.WRITABLE_BOOK);
        Optional.ofNullable(PluginConfig.ANSWERING.BOOK_ITEM.NAME.parse(player)).ifPresent(factory::setDisplayName);
        Optional.ofNullable(PluginConfig.ANSWERING.BOOK_ITEM.LORE.parse(player)).ifPresent(factory::setLore);

        if (existingPages != null) {
            return new BookUtil.BookBuilder(factory.toItemStack()).pagesRaw(existingPages).build();
        } else {
            return factory.toItemStack();
        }

    }

}
