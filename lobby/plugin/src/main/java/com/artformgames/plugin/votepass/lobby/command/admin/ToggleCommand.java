package com.artformgames.plugin.votepass.lobby.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends SubCommand<MainCommand> {

    public ToggleCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }
    
}
