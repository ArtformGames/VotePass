package com.artformgames.plugin.votepass.game.command.admin.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class UserAddCommand extends SubCommand<MainCommand> {

    public UserAddCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        UsersManager usersManager = Main.getInstance().getUserManager();

        String username = args[0];

        WhitelistedUserData data = usersManager.getWhitelistData(username);
        if (data != null) {
            PluginMessages.USERS.ALREADY_IN.send(sender, data.getKey().name());
            return null;
        }

        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();

            UserKey key = usersManager.getKey(username);
            if (key == null) {
                PluginMessages.USERS.NEVER_JOINED.send(sender, username);
                return;
            }

            PluginMessages.USERS.ADD.START.send(sender, key.name());
            try {
                Main.getInstance().getUserManager().modifyWhitelist().add(key).execute().get();
                PluginMessages.USERS.ADD.SUCCESS.send(sender, key.name(), System.currentTimeMillis() - s1);
            } catch (Exception e) {
                PluginMessages.USERS.ADD.FAILED.send(sender, key.name(), System.currentTimeMillis() - s1);
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
