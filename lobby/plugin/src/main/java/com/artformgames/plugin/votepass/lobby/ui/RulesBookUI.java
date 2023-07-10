package com.artformgames.plugin.votepass.lobby.ui;

import com.artformgames.plugin.votepass.lobby.api.data.server.ServerRules;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RulesBookUI {

    private RulesBookUI() {
    }

    public static void open(@NotNull Player player, @NotNull String serverID, @NotNull ServerRules rules) {

        BookUtil.BookBuilder builder = BookUtil.writtenBook();
        if (rules.title() != null) builder.title(rules.title());
        if (rules.author() != null) builder.author(rules.author());

        List<BaseComponent[]> finalPages = new ArrayList<>();

        Iterator<List<String>> pageIterator = rules.pages().iterator();
        while (pageIterator.hasNext()) {
            List<String> page = pageIterator.next();
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = page.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                if (iterator.hasNext()) sb.append("\n");
            }
            if (!pageIterator.hasNext()) {
                sb.append("\n");
                sb.append(PluginConfig.RULES.BOOK.ACCEPT.parse(player, serverID));
                sb.append("\n");
                sb.append(PluginConfig.RULES.BOOK.DENY.parse(player, serverID));
            }
            finalPages.add(MineDown.parse(sb.toString()));
        }

        builder.pages(finalPages);

        BookUtil.openPlayer(player, builder.build());
    }
}
