package com.artformgames.plugin.votepass.server;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.core.database.DataManager;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.server.api.user.GameUsersManager;
import com.artformgames.plugin.votepass.server.api.vote.VoteManager;
import com.artformgames.plugin.votepass.server.conf.PluginConfig;
import com.artformgames.plugin.votepass.server.conf.PluginMessages;
import org.jetbrains.annotations.NotNull;

public class Main extends VotePassPlugin implements VotePassServer {
    private static Main instance;

    public Main() {
        Main.instance = this;
        VotePassServerAPI.instance = this;
    }

    @Override
    public AbstractUserManager<?> getUserManager() {
        return null;
    }

    protected MineConfiguration configuration;

    protected DataManager dataManager;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);

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

        log("Initialize requests manager...");
    }

    @Override
    protected boolean initialize() {

        log("Register listeners...");

        log("Register commands...");

        loadMetrics();
        checkVersion();
        return true;
    }

    @Override
    protected void shutdown() {

        log("Shutting down UserManager...");
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
    public @NotNull GameUsersManager getUsersManager() {
        return null;
    }

    @Override
    public @NotNull VoteManager getVoteManager() {
        return null;
    }
}
