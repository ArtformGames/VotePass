package com.artformgames.plugin.votepass.lobby;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.core.database.DataManager;
import com.artformgames.plugin.votepass.lobby.api.request.UserRequestManager;
import com.artformgames.plugin.votepass.lobby.api.server.ServerSettingsManager;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import com.artformgames.plugin.votepass.lobby.listener.BookListener;
import com.artformgames.plugin.votepass.lobby.listener.FeedbackListener;
import com.artformgames.plugin.votepass.lobby.listener.UserListener;
import com.artformgames.plugin.votepass.lobby.request.RequestManager;
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

    protected DataManager dataManager;
    protected SettingsManager settingsManager;
    protected RequestManager requestManager;
    protected UsersManager usersManager;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);

        log("Loading servers configurations...");
        this.settingsManager = new SettingsManager(this);
        this.settingsManager.reloadSettings();

        log("Loading database...");
        this.dataManager = new DataManager(this);
        try {
            this.dataManager.initialize();
        } catch (Exception e) {
            error("Failed to initialize database, please check the configuration!");
            e.printStackTrace();
            setEnabled(false);
        }


        log("Initialize users manager...");
        this.usersManager = new UsersManager(this);


        log("Initialize requests manager...");
        this.requestManager = new RequestManager();

    }

    @Override
    protected boolean initialize() {

        log("Register listeners...");
        registerListener(new UserListener());
        registerListener(new BookListener());
        registerListener(new FeedbackListener());

        log("Register commands...");
        registerCommand(getName(), new MainCommand(this));

        loadMetrics();
        checkVersion();
        return true;
    }

    @Override
    protected void shutdown() {

        log("Shutting down UserManager...");
        this.usersManager.shutdown();

        log("Shutting down DataManager...");
        this.dataManager.shutdown();

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
        return this.usersManager;
    }

    public @NotNull DataManager getDataManager() {
        return this.dataManager;
    }

    @Override
    public @NotNull ServerSettingsManager getServersManager() {
        return this.settingsManager;
    }

    @Override
    public @NotNull UserRequestManager getRequestManager() {
        return this.requestManager;
    }
}
