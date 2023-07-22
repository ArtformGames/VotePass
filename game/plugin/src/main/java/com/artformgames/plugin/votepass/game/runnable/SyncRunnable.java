package com.artformgames.plugin.votepass.game.runnable;

import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncRunnable extends BukkitRunnable {

    protected static SyncRunnable runner = null;

    public static void start() {
        shutdown();
        long periods = PluginConfig.SERVER.SYNC_PERIOD.getNotNull().getSeconds() * 20;
        if (periods <= 0) return;

        runner = new SyncRunnable();
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
        int synced = Main.getInstance().getVoteManager().sync();
        Main.debugging("Successfully synced " + synced + " new requests from database.");
    }

}
