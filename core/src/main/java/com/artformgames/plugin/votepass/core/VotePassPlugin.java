package com.artformgames.plugin.votepass.core;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import com.artformgames.plugin.votepass.core.utils.GHUpdateChecker;
import org.bstats.bukkit.Metrics;

public abstract class VotePassPlugin extends EasyPlugin {

    protected VotePassPlugin() {
        super(EasyPluginMessageProvider.EN_US);
    }

    public abstract AbstractUserManager<?> getUserManager();

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
}
