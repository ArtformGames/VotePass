package com.artformgames.plugin.votepass.game.ui.user;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistedUserData;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.List;

public class AbstainToggleGUI {

    private AbstainToggleGUI() {
    }

    public static void open(Player player) {
        WhitelistedUserData user = Main.getInstance().getUserManager().getWhitelistData(player.getUniqueId());
        if (user == null) return;

        BookUtil.BookBuilder builder = BookUtil.writtenBook();
        builder.title("#");
        builder.author(player.getName());

        List<BaseComponent[]> pages = new ArrayList<>();
        if (user.isAbstained()) {
            pages.add(CONFIG.ENABLED.compileLine(player));
        } else {
            pages.add(CONFIG.DISABLED.compileLine(player));
        }

        pages.add(CONFIG.TOGGLE.compileLine(player));
        builder.pages(pages);

        BookUtil.openPlayer(player, builder.build());
    }

    public interface CONFIG extends Configuration {

        ConfiguredMessage<BaseComponent[]> ENABLED = TextMessages.create()
                .defaults(
                        "&8Your current status is",
                        "&e&labstaining all vote rights&8.",
                        " ",
                        "&7&oYou will not be calculated in server's whitelist review results.",
                        "&8Exercising voting rights should be something every member should do.",
                        " "
                ).build();

        ConfiguredMessage<BaseComponent[]> DISABLED = TextMessages.create()
                .defaults(
                        "&8Your current status is",
                        "&a&lParticipating in voting&8.",
                        " ",
                        "&7Thank you for your active participation in server management.",
                        " ",
                        " "
                ).build();
        ConfiguredMessage<BaseComponent[]> TOGGLE = TextMessages.create()
                .defaults(
                        "You can choose whether to automatically waive all future requests.",
                        " ",
                        "[&a&l[I'd like to vote]](hover=Click to return to participating in voting. run_command=/votepass abstain disable)",
                        "[&e&l[Abstain all]](hover=Click to abstain all future votes. run_command=/votepass abstain enable)"
                ).params("id")
                .build();


    }
}
