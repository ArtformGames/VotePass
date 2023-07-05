package com.artformgames.plugin.votepass.lobby;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import com.artformgames.plugin.votepass.lobby.api.server.ServerSettingsManager;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import com.artformgames.plugin.votepass.lobby.listener.BookListener;
import com.artformgames.plugin.votepass.lobby.listener.FeedbackListener;
import com.artformgames.plugin.votepass.lobby.listener.UserListener;
import com.artformgames.plugin.votepass.lobby.server.SettingsManager;
import com.artformgames.plugin.votepass.lobby.user.UsersManager;
import org.jetbrains.annotations.NotNull;

public class Main extends VotePassPlugin implements VotePassLobby {
    private static Main instance;

    public Main() {
        Main.instance = this;
        VotePassLobbyAPI.instance = this;
    }

    protected MineConfiguration configuration;

    protected SettingsManager lobbyServersManager;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);

        log("Loading servers configurations...");
        this.lobbyServersManager = new SettingsManager(this);
        this.lobbyServersManager.reloadSettings();


    }

    @Override
    protected boolean initialize() {

        log("初始化存储方式...");

        log("注册监听器...");
        registerListener(new UserListener());
        registerListener(new BookListener());
        registerListener(new FeedbackListener());

        log("注册指令...");
        registerCommand(getName(), new MainCommand(this));

        loadMetrics();
        checkVersion();
        return true;
    }

    @Override
    protected void shutdown() {

    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public static Main getInstance() {
        return instance;
    }

    public MineConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public @NotNull UsersManager getUserManager() {
        return null;
    }

    @Override
    public @NotNull ServerSettingsManager getServersManager() {
        return this.lobbyServersManager;
    }

    @Override
    public @NotNull UserRequestManager getRequestManager() {
        return null;
    }
}
