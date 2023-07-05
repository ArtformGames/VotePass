package com.artformgames.plugin.votepass.lobby.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends SubCommand<MainCommand> {

    public ToggleCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        ServerSettings settings = VotePassLobbyAPI.getServersManager().getSettings(args[0]);
        if (settings == null) return getParent().sendMessage(sender, PluginMessages.NOT_EXISTS, args[0]);

        boolean disabled = VotePassLobbyAPI.getServersManager().isDisabled(settings.id());
        VotePassLobbyAPI.getServersManager().setDisabled(settings.id(), !disabled);

        if (disabled) {
            PluginMessages.TOGGLE.ENABLED.send(sender, settings.name());
        } else {
            PluginMessages.TOGGLE.DISABLED.send(sender, settings.name());
        }
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}
