package com.artformgames.plugin.votepass.game.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.ui.admin.AdminHandleGUI;
import com.artformgames.plugin.votepass.game.ui.admin.AdminManageGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ManageCommand extends SubCommand<MainCommand> {

    public ManageCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player player)) return getParent().onlyPlayer(sender);
        if (args.length < 1) {
            AdminManageGUI.open(player);
            return null;
        }

        int id = -1;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
        }

        if (id <= 0) {
            CommonMessages.WRONG_NUMBER.send(player);
            return null;
        }

        RequestInformation request = Main.getInstance().getVoteManager().getRequest(id);
        if (request == null) {
            CommonMessages.NOT_EXISTS.send(player, id);
            return null;
        }
        AdminHandleGUI.open(player, request, null);
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}
