package com.artformgames.plugin.votepass.core.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public abstract class TextMessages extends ConfigurationRoot {

    public static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser()).whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    public static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser()).whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    public static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender instanceof Player player) message = PlaceholderAPI.setPlaceholders(player, message);
            return MineDown.parse(ColorParser.parse(message));
        };
    }

}
