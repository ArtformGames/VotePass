package com.artformgames.plugin.votepass.lobby.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import com.artformgames.plugin.votepass.lobby.gui.RequestingGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RuleAcceptCommand extends SubCommand<MainCommand> {

    public RuleAcceptCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
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

        LobbyUserData data = VotePassLobbyAPI.getUserManager().get(player.getUniqueId());

        if (RequestCommand.cannotCreate(player, data, settings)) return null;

        PendingRequest pendingRequest = data.getPendingRequest();
        if (pendingRequest != null && pendingRequest.getSettings().equals(settings)) {
            // Answering the corresponding question, let the player continue to answer
            RequestingGUI.open(player, data, pendingRequest);
        } else {
            PluginMessages.ACCEPTED.send(player, settings.name());
            RequestingGUI.open(player, data, data.createPendingRequest(settings));
        }

        return null;
    }


}
