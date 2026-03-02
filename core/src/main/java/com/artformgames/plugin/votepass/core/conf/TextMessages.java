package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface TextMessages extends Configuration {

    static ConfiguredMessage.@NotNull Builder<BaseComponent[]> create() {
        return ConfiguredMessage.create(getParser())
                .dispatcher((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return TextMessages::parse;
    }

    static @NotNull BaseComponent[] parse(@NotNull CommandSender sender, @NotNull String message) {
        if (sender instanceof Player player) message = PlaceholderAPI.setPlaceholders(player, message);
        return MineDown.parse(ColorParser.parse(message));
    }

}
