package com.artformgames.plugin.votepass.game.command.admin;

import cc.carm.lib.easyplugin.command.CommandHandler;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserAddCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserListCommand;
import com.artformgames.plugin.votepass.game.command.admin.user.UserRemoveCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class UsersCommands extends CommandHandler {

    protected MainCommand parent;

    public UsersCommands(@NotNull JavaPlugin plugin, MainCommand command, @NotNull String cmd, @NotNull String... alias) {
        super(plugin, cmd, alias);
        this.parent = command;

        registerSubCommand(new UserListCommand(this, "list"));
        registerSubCommand(new UserAddCommand(this, "add"));
        registerSubCommand(new UserRemoveCommand(this, "remove"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return this.parent.noArgs(sender);
    }

    @Override
    public Void noPermission(CommandSender sender) {
        return this.parent.noPermission(sender);
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }
}
