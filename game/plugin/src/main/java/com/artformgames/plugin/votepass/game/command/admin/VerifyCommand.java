package com.artformgames.plugin.votepass.game.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.vote.VoteManager;
import com.artformgames.plugin.votepass.game.command.MainCommand;
import com.artformgames.plugin.votepass.game.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class VerifyCommand extends SubCommand<MainCommand> {

    public VerifyCommand(@NotNull MainCommand parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        Main.getInstance().getScheduler().runAsync(() -> {
            long s1 = System.currentTimeMillis();

            PluginMessages.VERIFY.START.send(sender);
            VoteManager voteManager = Main.getInstance().getVoteManager();

            Set<RequestInformation> approved = new HashSet<>();
            Set<RequestInformation> rejected = new HashSet<>();
            for (RequestInformation request : voteManager.getRequests().values()) {
                if (voteManager.calculateResult(request) == RequestResult.APPROVED) {
                    approved.add(request);
                } else {
                    rejected.add(request);
                }
            }

            approved.forEach(requestInformation -> voteManager.approve(requestInformation).join());
            rejected.forEach(requestInformation -> voteManager.reject(requestInformation).join());

            PluginMessages.VERIFY.SUCCESS.prepare(
                    approved.size() + rejected.size(),
                    System.currentTimeMillis() - s1
            ).to(sender);
        });
        return null;
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

}
