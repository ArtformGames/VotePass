package com.artformgames.plugin.votepass.lobby.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RuleDenyCommand extends SubCommand<MainCommand> {

    public RuleDenyCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return getParent().onlyPlayer(sender);
        if (args.length < 1) return getParent().noArgs(sender);

        ServerSettings settings = VotePassLobbyAPI.getServersManager().getSettings(args[0]);
        if (settings == null) return getParent().sendMessage(sender, PluginMessages.NOT_EXISTS, args[0]);

        if (VotePassLobbyAPI.getServersManager().isDisabled(settings.id())) {
            return getParent().sendMessage(sender, PluginMessages.DISABLED, settings.id());
        }

        PluginMessages.REJECTED.send(player, settings.name());
        return null;
    }


}
