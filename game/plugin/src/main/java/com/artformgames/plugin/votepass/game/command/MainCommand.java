package com.artformgames.plugin.votepass.game.command;

import com.artformgames.plugin.votepass.core.command.VotePassCommand;
import com.artformgames.plugin.votepass.game.command.admin.ManageCommand;
import com.artformgames.plugin.votepass.game.command.admin.MigrateCommand;
import com.artformgames.plugin.votepass.game.command.admin.ReloadCommand;
import com.artformgames.plugin.votepass.game.command.admin.SyncCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserAddCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserListCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserRemoveCommand;
import com.artformgames.plugin.votepass.game.command.user.AbstainCommand;
import com.artformgames.plugin.votepass.game.command.user.HandleCommand;
import com.artformgames.plugin.votepass.game.command.user.RequestsCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends VotePassCommand {

    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);

        registerSubCommand(new RequestsCommand(this, "requests", "request"));
        registerSubCommand(new HandleCommand(this, "handle"));
        registerSubCommand(new AbstainCommand(this, "abstain", "abstained"));

        registerSubCommand(new ManageCommand(this, "manage"));
        registerSubCommand(new MigrateCommand(this, "migrate"));
        registerSubCommand(new SyncCommand(this, "sync"));
        registerSubCommand(new ReloadCommand(this, "reload"));

        registerSubCommand(new UserListCommand(this, "list"));
        registerSubCommand(new UserAddCommand(this, "add"));
        registerSubCommand(new UserRemoveCommand(this, "remove"));

    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.COMMAND.USER.send(sender);
        if (sender.hasPermission("votepass.admin")) {
            PluginMessages.COMMAND.ADMIN.send(sender);
        }
        return null;
    }

}
