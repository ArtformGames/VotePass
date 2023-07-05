package com.artformgames.plugin.votepass.lobby.command;

import com.artformgames.plugin.votepass.core.command.VotePassCommand;
import com.artformgames.plugin.votepass.lobby.command.admin.ReloadCommand;
import com.artformgames.plugin.votepass.lobby.command.admin.ToggleCommand;
import com.artformgames.plugin.votepass.lobby.command.user.RequestCommand;
import com.artformgames.plugin.votepass.lobby.command.user.RuleAcceptCommand;
import com.artformgames.plugin.votepass.lobby.command.user.RuleDenyCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends VotePassCommand {

    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);

        registerSubCommand(new RequestCommand(this, "request"));
        registerSubCommand(new RuleAcceptCommand(this, "accept"));
        registerSubCommand(new RuleDenyCommand(this, "deny"));

        registerSubCommand(new ReloadCommand(this, "reload"));
        registerSubCommand(new ToggleCommand(this, "toggle"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return null;
    }

}
