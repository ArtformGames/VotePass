package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;

public class PluginMessages extends TextMessages {

    public static class VOTE extends ConfigurationRoot {

        public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_VILLAGER_CELEBRATE);

        public static final ConfiguredMessageList<BaseComponent[]> NOT_VOTED = list()
                .defaults(
                        "&fYou are a whitelist user and should exercise the right to vote for the server whitelist review, but you still have &e%(amount) &f pieces of whitelist review votes that you have not participated in.",
                        "&fYou can [&6&lCLICK HERE](show_text=click to open the whitelist voting interface run_command=/votepass requests) &fParticipate in the server whitelist review and exercise your voting right!"
                ).params("amount").build();
        public static final ConfiguredMessageList<BaseComponent[]> VIEWING = list()
                .defaults(
                        "&fYou are viewing &e%(player) &f's answer to &6%(question) &f,",
                        "&fYou can [&6&lCLICK HERE](show_text=Click to open the request details page run_command=/votepass handle %(id)) &fto return to the request details page."
                ).params("id", "player", "question").build();

        public static final ConfiguredMessageList<BaseComponent[]> APPROVED = list()
                .defaults(
                        "&fYou have successfully voted &a&lAPPROVED &f for &e%(player) &f's whitelist application &8(#%(id))&f!",
                        "&f Thank you for participating in server management as a member of the server and exercising your voting rights."
                ).params("id", "player").build();

        public static final ConfiguredMessageList<BaseComponent[]> REJECTED = list()
                .defaults(
                        "&fYou have successfully voted &c&lREJECTED &f for &e%(player) &f's whitelist application &8(#%(id))&f!",
                        "&f Thank you for participating in server management as a member of the server and exercising your voting rights."
                ).params("id", "player").build();

        public static final ConfiguredMessageList<BaseComponent[]> ABSTAINED = list()
                .defaults(
                        "&fYou have successfully &c&lABSTAINED &f for &e%(player) &f's whitelist application &8(#%(id))&f,",
                        "&fThat you will not be calculated in server's whitelist review results.",
                        "&7&o If you are not interested in server management as a member of the server and exercising your voting rights," +
                                " you can simply type &e&l/votepass abstain toggle &7&oto abstain all the requests in the future."
                ).params("id", "player").build();

    }

    public static class ADMIN extends ConfigurationRoot {

        public static final ConfiguredSound SOUND = ConfiguredSound.of(Sound.ENTITY_VILLAGER_CELEBRATE);

        public static final ConfiguredMessageList<BaseComponent[]> INTERVENTION = list()
                .defaults(
                        "&fThe current server still has &e%(amount) &f whitelist requests that have not been voted within the specified time, and administrator intervention may be required.",
                        "&fYou can directly [&6&lCLICK HERE](show_text=click to open the administrator audit interface run_command=/votepass admin) &fIntervene in the server whitelist audit."
                ).params("amount").build();


        public static final ConfiguredMessageList<BaseComponent[]> APPROVED = list()
                .defaults(
                        "&fYou have successfully &a&lpassed &e%(player) &f's whitelist application &8(#%(id))&f, and the corresponding request has been automatically closed."
                ).params("id", "player").build();

        public static final ConfiguredMessageList<BaseComponent[]> REJECTED = list()
                .defaults(
                        "&fYou have successfully &c&lrejected &e%(player) &f's whitelist application &8(#%(id))&f, and the corresponding request has been automatically closed."
                ).params("id", "player").build();

    }

}
