package com.artformgames.plugin.votepass.game.runnable;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.vote.VoteManagerImpl;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

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
        VoteManagerImpl manager = Main.getInstance().getVoteManager();
        int synced = manager.sync();
        Main.debugging("Successfully synced " + synced + " new requests from database.");

        Duration timeoutTime = PluginConfig.TIME.AUTO_CLOSE.getNotNull();
        Set<RequestInformation> autoClosed = manager.getRequests().values().stream()
                .filter(request -> request.isTimeout(timeoutTime)).collect(Collectors.toSet());
        autoClosed.forEach(timeoutRequest -> manager.updateResult(timeoutRequest, RequestResult.EXPIRED).join());

        Main.debugging("Successfully auto closed " + autoClosed.size() + " timeout requests.");
    }

}
