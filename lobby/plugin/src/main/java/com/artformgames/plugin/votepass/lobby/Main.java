package com.artformgames.plugin.votepass.lobby;

import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.lobby.api.server.LobbyServersManager;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserManager;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import com.artformgames.plugin.votepass.lobby.server.ServersManager;
import org.jetbrains.annotations.NotNull;

public class Main extends VotePassPlugin implements VotePassLobby {
    private static Main instance;

    public Main() {
        Main.instance = this;
        VotePassLobbyAPI.instance = this;
    }

    protected MineConfiguration configuration;

    protected ServersManager serversManager;

    @Override
    protected void load() {

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this, PluginConfig.class, PluginMessages.class);

        log("Loading servers configurations...");
        this.serversManager = new ServersManager(this);
        this.serversManager.reloadApplications();


    }

    @Override
    protected boolean initialize() {

        log("初始化存储方式...");

        log("注册监听器...");

        log("注册指令...");


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
    public @NotNull LobbyUserManager getUserManager() {
        return null;
    }

    @Override
    public @NotNull LobbyServersManager getServersManager() {
        return this.serversManager;
    }
}
