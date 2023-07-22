package com.artformgames.plugin.votepass.game.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistModifier;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class MigrateCommand extends SubCommand<MainCommand> {

    public MigrateCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        UsersManager usersManager = Main.getInstance().getUserManager();


        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();
            PluginMessages.MIGRATE.START.send(sender);

            WhitelistModifier modifier = usersManager.modifyWhitelist();
            for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
                try {
                    modifier.add(usersManager.upsertKey(player.getUniqueId(), player.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                int changes = modifier.execute().get();
                PluginMessages.MIGRATE.SUCCESS.send(sender, changes, System.currentTimeMillis() - s1);
            } catch (InterruptedException | ExecutionException e) {
                PluginMessages.MIGRATE.FAILED.send(sender);
                e.printStackTrace();
            }
        });

        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}