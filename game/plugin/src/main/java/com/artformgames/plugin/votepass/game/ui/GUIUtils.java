package com.artformgames.plugin.votepass.game.ui;

import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import org.bukkit.entity.Player;

public class GUIUtils {

    public static void loadPageIcon(AutoPagedGUI gui, Player player,
                                    int previousSlot, int nextSlot) {
        gui.setPreviousPageSlot(previousSlot);
        gui.setNextPageSlot(nextSlot);
        gui.setNextPageUI(CommonConfig.PAGE_ITEMS.NEXT_PAGE.get(player));
        gui.setPreviousPageUI(CommonConfig.PAGE_ITEMS.PREVIOUS_PAGE.get(player));
    }


}
