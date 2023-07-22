package com.artformgames.plugin.votepass.game.command;

import com.artformgames.plugin.votepass.core.command.VotePassCommand;
import com.artformgames.plugin.votepass.game.command.admin.*;
import com.artformgames.plugin.votepass.game.command.user.AbstainCommand;
import com.artformgames.plugin.votepass.game.command.user.HandleCommand;
import com.artformgames.plugin.votepass.game.command.user.RequestsCommand;
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
        registerHandler(new UsersCommands(plugin, this, "user", "users"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return null;
    }

}
