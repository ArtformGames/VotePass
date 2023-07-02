package com.artformgames.plugin.votepass.core.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class VotePassCommand extends CommandHandler {

    protected VotePassCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return sendMessage(sender, CommonMessages.NO_PERMISSION);
    }

    public Void sendMessage(CommandSender sender, ConfiguredMessage<?> message, Object... values) {
        message.send(sender, values);
        return null;
    }

    public Void sendMessage(CommandSender sender, ConfiguredMessageList<?> message, Object... values) {
        message.send(sender, values);
        return null;
    }

}
