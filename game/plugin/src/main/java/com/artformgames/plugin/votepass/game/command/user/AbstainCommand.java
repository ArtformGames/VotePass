package com.artformgames.plugin.votepass.game.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.ui.user.AbstainToggleGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AbstainCommand extends SubCommand<MainCommand> {

    public AbstainCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return getParent().onlyPlayer(sender);

        WhitelistedUserData user = Main.getInstance().getUserManager().getWhitelistData(player.getUniqueId());
        if (user == null) {
            CommonMessages.LOAD_FAILED.send(player);
            return null;
        }

        if (args.length == 0) {
            AbstainToggleGUI.open(player);
            return null;
        } else {
            String aim = args[0];
            if (aim.equalsIgnoreCase("enable")) {
                user.setAbstained(true);
                PluginMessages.ABSTAIN.ABSTAINED.send(player);
                return null;
            } else if (aim.equalsIgnoreCase("disable")) {
                user.setAbstained(false);
                PluginMessages.ABSTAIN.PARTICIPATING.send(player);
                return null;
            } else {
                return getParent().noArgs(sender);
            }
        }

    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.abstain");
    }

}