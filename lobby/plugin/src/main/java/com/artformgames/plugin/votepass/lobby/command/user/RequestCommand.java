package com.artformgames.plugin.votepass.lobby.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.data.request.RequestInfo;
import com.artformgames.plugin.votepass.lobby.VotePassLobbyAPI;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.data.user.PendingRequest;
import com.artformgames.plugin.votepass.lobby.api.user.LobbyUserData;
import com.artformgames.plugin.votepass.lobby.command.MainCommand;
import com.artformgames.plugin.votepass.lobby.conf.PluginMessages;
import com.artformgames.plugin.votepass.lobby.gui.RequestingGUI;
import com.artformgames.plugin.votepass.lobby.gui.RulesBookUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RequestCommand extends SubCommand<MainCommand> {

    public RequestCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player player)) return getParent().onlyPlayer(sender);
        if (args.length < 1) return getParent().noArgs(sender);

        ServerSettings settings = VotePassLobbyAPI.getServersManager().getSettings(args[0]);
        if (settings == null) return getParent().sendMessage(sender, PluginMessages.NOT_EXISTS, args[0]);

        if (VotePassLobbyAPI.getServersManager().isDisabled(settings.id())) {
            return getParent().sendMessage(sender, PluginMessages.DISABLED, settings.id());
        }

        LobbyUserData requestData = VotePassLobbyAPI.getUserManager().get(player.getUniqueId());

        if (RequestCommand.cannotCreate(player, requestData, settings)) return null;

        if (settings.rules() != null) {
            PluginMessages.RULES.send(player, settings.name());
            RulesBookUI.open(player, settings.id(), settings.rules());
        } else {
            PendingRequest pendingRequest = requestData.getPendingRequest();
            if (pendingRequest != null && pendingRequest.getSettings().equals(settings)) {
                RequestingGUI.open(player, requestData, pendingRequest);
            } else {
                PluginMessages.ACCEPTED.send(player, settings.name());
                RequestingGUI.open(player, requestData, requestData.createPendingRequest(settings));
            }

        }

        return null;
    }

    protected static boolean cannotCreate(Player player, LobbyUserData requestData, ServerSettings settings) {
        RequestInfo request = requestData.getServerRequest(settings.id());
        if (request != null) {
            PluginMessages.PENDING.send(player, request, settings.name());
            return true;
        }

        if (requestData.isPassed(settings.id())) {
            PluginMessages.WHITELISTED.send(player, settings.name());
            return true;
        }
        return false;
    }
}
