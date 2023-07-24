package com.artformgames.plugin.votepass.core;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.core.utils.GHUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

public abstract class VotePassPlugin extends EasyPlugin {

    MineConfiguration configuration;

    protected VotePassPlugin() {
        super(EasyPluginMessageProvider.EN_US);
    }

    public abstract AbstractUserManager<?> getUserManager();

    public void initializeConfigs(@NotNull Class<? extends ConfigurationRoot> configRoot,
                                  @NotNull Class<? extends ConfigurationRoot> messageRoot) {
        this.configuration = new MineConfiguration(this, CommonConfig.class, CommonMessages.class);
        this.configuration.initializeConfig(configRoot);
        this.configuration.initializeMessage(messageRoot);
    }

    public void loadMetrics() {
        if (CommonConfig.METRICS.getNotNull()) {
            log("Initializing bStats...");
            new Metrics(this, 18946);
        }
    }

    public void checkVersion() {
        if (CommonConfig.CHECK_UPDATE.getNotNull()) {
            log("Start to check the plugin versions...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("Version checker is disabled, skipped.");
        }
    }

    @Override
    public boolean isDebugging() {
        return CommonConfig.DEBUG.getNotNull();
    }

    public MineConfiguration getConfiguration() {
        return configuration;
    }

}
