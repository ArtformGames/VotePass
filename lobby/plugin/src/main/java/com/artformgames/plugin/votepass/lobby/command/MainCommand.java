package com.artformgames.plugin.votepass.lobby.command;

import com.artformgames.plugin.votepass.core.command.VotePassCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends VotePassCommand {

    protected MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return null;
    }

}
