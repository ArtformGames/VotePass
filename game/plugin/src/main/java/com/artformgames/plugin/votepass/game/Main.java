package com.artformgames.plugin.votepass.game;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.core.database.DataManager;
import com.artformgames.plugin.votepass.core.listener.UserListener;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.listener.NotifyListener;
import com.artformgames.plugin.votepass.game.listener.WhitelistListener;
import com.artformgames.plugin.votepass.game.runnable.NotifyRunnable;
import com.artformgames.plugin.votepass.game.runnable.SyncRunnable;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import com.artformgames.plugin.votepass.game.vote.VoteManagerImpl;
import dev.pns.signapi.SignAPI;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class Main extends VotePassPlugin implements VotePassServer {
    private static Main instance;

    public Main() {
        Main.instance = this;
        VotePassServerAPI.instance = this;
    }

    protected MineConfiguration configuration;

    protected DataManager dataManager;
    protected UsersManager usersManager;
    protected VoteManagerImpl voteManager;

    protected SignAPI signAPI;

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
        this.usersManager = new UsersManager(this);

        log("Initialize vote manager...");
        this.voteManager = new VoteManagerImpl();
    }

    @Override
    protected boolean initialize() {

        log("Initialize sign api...");
        this.signAPI = new SignAPI(this);

        log("Register listeners...");
        registerListener(new UserListener<>(getUserManager()));
        registerListener(new WhitelistListener());
        registerListener(new NotifyListener());

        log("Register commands...");
        registerCommand(getName(), new MainCommand(this));

        loadMetrics();
        checkVersion();

        log("Start runners...");
        NotifyRunnable.start();
        SyncRunnable.start();

        return true;
    }

    @Override
    protected void shutdown() {

        log("Shutdown runners...");
        NotifyRunnable.shutdown();
        SyncRunnable.shutdown();

        log("Shutting down UserManager...");
        try {
            this.usersManager.saveAll().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
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
    public @NotNull String getServerID() {
        return PluginConfig.SERVER.ID.getNotNull();
    }

    @Override
    public @NotNull UsersManager getUserManager() {
        return this.usersManager;
    }

    @Override
    public @NotNull VoteManagerImpl getVoteManager() {
        return this.voteManager;
    }

    public @NotNull DataManager getDataManager() {
        return this.dataManager;
    }

    public static SignAPI getSignAPI() {
        return getInstance().signAPI;
    }
}
