package com.artformgames.plugin.votepass.game.runnable;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.listener.NotifyListener;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class NotifyRunnable extends BukkitRunnable {

    protected static NotifyRunnable runner = null;

    public static void start() {
        shutdown();
        long periods = PluginConfig.SERVER.NOTIFY_PERIOD.getNotNull().getSeconds() * 20;
        if (periods <= 0) return;

        runner = new NotifyRunnable();
        runner.runTaskTimerAsynchronously(Main.getInstance(), periods, periods);
    }

    public static void shutdown() {
        if (runner != null) {
            runner.cancel();
            runner = null;
        }
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(NotifyListener::notify);
    }

}
