package com.artformgames.plugin.votepass.core;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.configuration.source.yaml.YAMLConfigFactory;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.core.utils.GHUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class VotePassPlugin extends EasyPlugin {

    ConfigurationHolder<?> configHolder;
    ConfigurationHolder<?> messageHolder;

    protected VotePassPlugin() {
        super(EasyPluginMessageProvider.EN_US);
    }

    public abstract AbstractUserManager<?> getUserManager();

    public void initializeConfigs(@NotNull Class<? extends Configuration> configRoot,
                                  @NotNull Class<? extends Configuration> messageRoot) {
        this.configHolder = YAMLConfigFactory.from(new File(getDataFolder(), "config.yml")).build();
        this.configHolder.initialize(configRoot);
        this.configHolder.initialize(CommonConfig.class);

        this.messageHolder = YAMLConfigFactory.from(new File(getDataFolder(), "messages.yml")).build();
        this.messageHolder.initialize(messageRoot);
        this.messageHolder.initialize(CommonMessages.class);
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

    public void reload() throws Exception {
        this.configHolder.reload();
        this.messageHolder.reload();
    }

    public void save() throws Exception {
        this.configHolder.save();
        this.messageHolder.save();
    }

}
