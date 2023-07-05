package com.artformgames.plugin.votepass.lobby.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommand> {

    public ReloadCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {

        try {
            CommonMessages.RELOAD.START.send(sender);
            long s1 = System.currentTimeMillis();

            Main.getInstance().getConfiguration().reload();
            Main.getInstance().getServersManager().reloadSettings();

            CommonMessages.RELOAD.SUCCESS.send(sender, System.currentTimeMillis() - s1);
        } catch (Exception e) {
            CommonMessages.RELOAD.FAILED.send(sender);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}
