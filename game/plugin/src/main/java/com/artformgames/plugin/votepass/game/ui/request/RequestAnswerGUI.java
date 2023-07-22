package com.artformgames.plugin.votepass.game.ui.request;

import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestAnswerGUI {

    private RequestAnswerGUI() {
    }

    public static void open(@NotNull Player player,
                            @NotNull RequestInformation request, @NotNull RequestAnswer answer,
                            @NotNull BaseComponent[]... extraPages) {
        BookUtil.openPlayer(player, generateBook(request, answer, extraPages));
    }

    public static ItemStack generateBook(RequestInformation request, RequestAnswer answer,
                                         BaseComponent[]... extraPages) {
        BookUtil.BookBuilder builder = BookUtil.writtenBook().title("#" + request.getID()).author(request.getUserDisplayName());

        List<BaseComponent[]> pages = new ArrayList<>();
        answer.answers().forEach(s -> pages.add(BookUtil.PageBuilder.of(s).build()));
        if (extraPages != null && extraPages.length > 0) pages.addAll(Arrays.asList(extraPages));
        builder.pages(pages);

        return builder.build();
    }

}
