package com.artformgames.plugin.votepass.game.command.admin.user;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.user.UserData;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class UserRemoveCommand extends SubCommand<MainCommand> {

    public UserRemoveCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 1) return getParent().noArgs(sender);

        UsersManager usersManager = Main.getInstance().getUserManager();

        String username = args[0];

        WhitelistedUserData data = usersManager.getWhitelistData(username);
        if (data == null) {
            PluginMessages.USERS.NOT_IN.send(sender, username);
            return null;
        }

        Player online = Bukkit.getPlayer(data.getUserUUID());
        if (online != null && online.isOnline()) {
            online.kickPlayer(PluginConfig.SERVER.KICK_MESSAGE.parseToLine(online));
        }

        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();

            UserKey key = data.getKey();

            PluginMessages.USERS.REMOVE.START.send(sender, key.name());
            try {
                Main.getInstance().getUserManager().modifyWhitelist().remove(key).execute();
                PluginMessages.USERS.REMOVE.SUCCESS.send(sender, key.name(), System.currentTimeMillis() - s1);
            } catch (Exception e) {
                PluginMessages.USERS.REMOVE.FAILED.send(sender, key.name(), System.currentTimeMillis() - s1);
                e.printStackTrace();
            }

        });
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.objects(args[0], Main.getInstance().getUserManager()
                    .getWhitelists().stream()
                    .map(UserData::getKey).map(UserKey::name)
                    .filter(Objects::nonNull));
        } else return SimpleCompleter.none();
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}