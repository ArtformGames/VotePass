package com.artformgames.plugin.votepass.game.command.admin.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.command.admin.UsersCommands;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class UserRemoveCommand extends SubCommand<UsersCommands> {

    public UserRemoveCommand(@NotNull UsersCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        UsersManager usersManager = Main.getInstance().getUserManager();

        String username = args[0];

        WhitelistedUserData data = usersManager.getWhitelistData(username);
        if (data == null) {
            PluginMessages.USERS.NOT_IN.send(sender, username);
            return null;
        }

        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();

            UserKey key = data.getKey();

            PluginMessages.USERS.REMOVE.START.send(sender, key.name());
            try {
                Main.getInstance().getUserManager().modifyWhitelist().remove(key).execute().get();
                PluginMessages.USERS.REMOVE.SUCCESS.send(sender, key.name(), System.currentTimeMillis() - s1);
            } catch (Exception e) {
                PluginMessages.USERS.REMOVE.FAILED.send(sender, key.name(), System.currentTimeMillis() - s1);
                e.printStackTrace();
            }

        });
        return null;
    }

}