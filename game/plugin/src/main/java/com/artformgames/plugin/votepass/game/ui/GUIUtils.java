package com.artformgames.plugin.votepass.game.ui;

import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.item.PreparedItem;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GUIUtils {

    public static ItemStack loadAnswersIcon(Player player,
                                            RequestAnswer answer, ConfiguredItem itemConfig) {
        int maxLine = PluginConfig.ANSWERS.MAX_LINES.getNotNull();
        PreparedItem item = itemConfig.prepare(answer.question(), answer.countWords());

        item.insertLore("question", sortContent(
                answer.question(), PluginConfig.ANSWERS.LETTERS_PER_LINE.getNotNull()
        ));
        
        List<String> lore = GUIUtils.formatAnswersLore(answer);
        if (lore.size() > maxLine) {
            item.insertLore("contents", lore.subList(0, maxLine), true);
            item.insertLore("more-contents", PluginConfig.ANSWERS.EXTRA);
        } else if (!lore.isEmpty()) {
            item.insertLore("contents", lore, true);
        }
        return item.get(player);
    }

    public static void loadPageIcon(AutoPagedGUI gui, Player player,
                                    int previousSlot, int nextSlot) {
        gui.setPreviousPageSlot(previousSlot);
        gui.setNextPageSlot(nextSlot);
        gui.setNextPageUI(CommonConfig.PAGE_ITEMS.NEXT_PAGE.get(player));
        gui.setPreviousPageUI(CommonConfig.PAGE_ITEMS.PREVIOUS_PAGE.get(player));
    }

    public static List<String> formatAnswersLore(@NotNull RequestAnswer answers) {
        return formatAnswersLore(answers.answers());
    }

    public static List<String> formatAnswersLore(@NotNull List<String> answers) {
        return formatAnswersLore(answers, PluginConfig.ANSWERS.LETTERS_PER_LINE.getNotNull());
    }

    public static List<String> formatAnswersLore(List<String> answers, int lettersPreLine) {
        return answers.stream()
                .flatMap(answer -> sortContent(answer, lettersPreLine).stream())
                .collect(Collectors.toList());
    }

    public static List<String> formatCommentLine(String content) {
        return sortContent(content, PluginConfig.COMMENT.LINE.getNotNull());
    }

    public static List<String> formatCommentLine(PendingVote vote) {
        return formatCommentLine(vote.getComments());
    }

    public static List<String> sortContent(String content, int lineLength) {
        List<String> lore = new ArrayList<>();
        if (content == null || content.isBlank()) return lore;

        content = content.replaceAll(Pattern.quote("§"), "&");// Prevent color problems

        int length = content.length();
        int lines = length / lineLength + (length % lineLength == 0 ? 0 : 1);
        for (int i = 0; i < lines; i++) {
            int start = i * lineLength;
            int end = Math.min((i + 1) * lineLength, length);
            lore.add(content.substring(start, end));
        }

        return lore;
    }

}
