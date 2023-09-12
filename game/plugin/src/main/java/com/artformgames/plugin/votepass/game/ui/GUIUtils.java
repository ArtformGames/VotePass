package com.artformgames.plugin.votepass.game.ui;

import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import com.artformgames.plugin.votepass.game.api.vote.PendingVote;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GUIUtils {

    public static void loadPageIcon(AutoPagedGUI gui, Player player,
                                    int previousSlot, int nextSlot) {
        gui.setPreviousPageSlot(previousSlot);
        gui.setNextPageSlot(nextSlot);
        gui.setNextPageUI(CommonConfig.PAGE_ITEMS.NEXT_PAGE.get(player));
        gui.setPreviousPageUI(CommonConfig.PAGE_ITEMS.PREVIOUS_PAGE.get(player));
    }

    public static List<String> formatAnswersLore(RequestAnswer answers) {
        return formatAnswersLore(answers.answers());
    }

    public static List<String> formatAnswersLore(List<String> answers) {

        int lettersPreLine = PluginConfig.ANSWERS.LETTERS_PER_LINE.getNotNull();

        String prefix = PluginConfig.ANSWERS.PREFIX.getNotNull();

        List<String> lore = new ArrayList<>();
        for (String answer : answers) {
            String cleared = answer
                    .replaceAll("%+([一-龥_a-zA-Z0-9-]+)%+", "$1")
                    .replaceAll("&", "&&").replaceAll(Pattern.quote("§"), "&&")
                    .replaceAll("^&+$", "");// Prevent color problems
            if (cleared.isBlank()) continue;

            int length = cleared.length();
            int lines = length / lettersPreLine + (length % lettersPreLine == 0 ? 0 : 1);
            for (int i = 0; i < lines; i++) {
                int start = i * lettersPreLine;
                int end = Math.min((i + 1) * lettersPreLine, length);
                lore.add(prefix + cleared.substring(start, end));
            }
        }
        return lore;
    }


    public static List<String> formatCommentLine(String content) {
        List<String> lore = new ArrayList<>();
        if (content == null || content.isBlank()) return lore;

        int line = PluginConfig.COMMENT.LINE.getNotNull();
        String prefix = PluginConfig.COMMENT.PREFIX.getNotNull();

        int length = content.length();
        int lines = length / line + (length % line == 0 ? 0 : 1);
        for (int i = 0; i < lines; i++) {
            int start = i * line;
            int end = Math.min((i + 1) * line, length);
            lore.add(prefix + content.substring(start, end));
        }

        return lore;
    }

    public static List<String> formatCommentLine(PendingVote vote) {
        return formatCommentLine(vote.getComments());
    }


}
