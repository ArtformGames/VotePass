package com.artformgames.plugin.votepass.game.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SyncCommand extends SubCommand<MainCommand> {

    public SyncCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();
            PluginMessages.SYNC.START.send(sender);
            int synced = Main.getInstance().getVoteManager().sync();
            PluginMessages.SYNC.SUCCESS.send(sender, synced, System.currentTimeMillis() - s1);
        });
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}
