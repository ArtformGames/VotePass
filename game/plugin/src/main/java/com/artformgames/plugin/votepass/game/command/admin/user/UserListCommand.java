package com.artformgames.plugin.votepass.game.command.admin.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class UserListCommand extends SubCommand<MainCommand> {

    public UserListCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        var list = Main.getInstance().getUserManager().getWhitelists();
        PluginMessages.USERS.LIST.send(sender, list.size());
        list.forEach(user -> PluginMessages.USERS.USER.send(sender, user.getKey().getDisplayName(), user.getKey().uuid(), user.getPassedTimeString()));
        return null;
    }
}
