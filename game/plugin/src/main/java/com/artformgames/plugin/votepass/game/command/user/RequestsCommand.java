package com.artformgames.plugin.votepass.game.command.user;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.ui.vote.RequestListGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RequestsCommand extends SubCommand<MainCommand> {

    public RequestsCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player player)) return getParent().onlyPlayer(sender);

        WhitelistedUserData user = Main.getInstance().getUserManager().getWhitelistData(player.getUniqueId());
        if (user == null) {
            CommonMessages.LOAD_FAILED.send(player);
            return null;
        }

        if (user.isAbstained()) {
            PluginMessages.ABSTAIN.ABSTAINED.send(player);
            return null;
        }

        RequestListGUI.open(player);
        return null;
    }
}
