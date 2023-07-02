package com.artformgames.plugin.votepass.lobby.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RuleDenyCommand extends SubCommand<MainCommand> {

    public RuleDenyCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        return null;
    }


}
