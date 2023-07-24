package com.artformgames.plugin.votepass.game.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Sound;

public class PluginMessages extends TextMessages {

    public static final class COMMAND extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> USER = list()
                .defaults(
                        "&e&lVotePass &fCommands &7(/votepass)",
                        "&8#&f requests",
                        "&8-&7 View all whitelist requests that have not been participated in.",
                        "&8#&f handle &e<requestID>",
                        "&8-&7 (Continue to) process the specified whitelist request.",
                        "&8#&f abstain",
                        "&8-&7 Enable/Disable the auto abstain function."
                ).build();

        public static final ConfiguredMessageList<BaseComponent[]> ADMIN = list()
                .defaults(
                        "&8#&f manage",
                        "&8-&7 Open the admin manage GUI.",
                        "&8#&f sync",
                        "&8-&7 Synchronize requests from database.",
                        "&8#&f migrate",
                        "&8-&7 Migrate the whitelist of the server",
                        "&8#&f list",
                        "&8-&7 List all players in whitelist.",
                        "&8#&f add <username>",
                        "&8-&7 Add a player to the whitelist.",
                        "&8#&f remove <username>",
                        "&8-&7 Remove a player from the whitelist.",
                        "&8#&f reload",
                        "&8-&7 Reload the configuration file."
                ).build();
    }

    public static class USERS extends ConfigurationRoot {
        public static final ConfiguredMessageList<BaseComponent[]> NEVER_JOINED = list()
                .defaults(
                        "&fUser %(name) is never joined in the server,",
                        "&fOnly the user had joined any server before can add to the whitelist.",
                        "&7&oAny server means lobby or any other game servers."
                ).params("name").build();

        public static final ConfiguredMessageList<BaseComponent[]> LIST = list()
                .defaults("&fServer users list &7(total &F%(total)&7):")
                .params("total").build();

        public static final ConfiguredMessageList<BaseComponent[]> USER = list()
                .defaults("&8- [&f%(name) &7[#%(request)]](hover=&7Joined at &f%(passed_time)))")
                .params("name", "uuid", "request", "passed_time").build();

        public static final ConfiguredMessageList<BaseComponent[]> ALREADY_IN = list()
                .defaults(
                        "&fUser %(name) is already in the whitelist."
                ).params("name").build();

        public static final ConfiguredMessageList<BaseComponent[]> NOT_IN = list()
                .defaults(
                        "&fUser %(name) is not in the whitelist."
                ).params("name").build();

        public static final class ADD extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> START = list()
                    .defaults(
                            "&fTry to add user %(name) to whitelist...",
                            "&fThis may take a while, please wait patiently."
                    ).params("name").build();

            public static final ConfiguredMessageList<BaseComponent[]> SUCCESS = list()
                    .defaults(
                            "&a&lSuccess! &fUser &e%(name) &fhas been added to whitelist, cost &a%(time)&fms."
                    ).params("amount", "time").build();

            public static final ConfiguredMessageList<BaseComponent[]> FAILED = list()
                    .defaults(
                            "&c&lFailed to add! &fThere are some errors, please see console for details."
                    ).params("amount", "time").build();
        }

        public static final class REMOVE extends ConfigurationRoot {

            public static final ConfiguredMessageList<BaseComponent[]> START = list()
                    .defaults(
                            "&fTry to remove user %(name) from whitelist...",
                            "&fThis may take a while, please wait patiently."
                    ).params("name").build();

            public static final ConfiguredMessageList<BaseComponent[]> SUCCESS = list()
                    .defaults(
                            "&a&lSuccess! &fUser &e%(name) &fhas been removed from whitelist, cost &a%(time)&fms."
                    ).params("amount", "time").build();

            public static final ConfiguredMessageList<BaseComponent[]> FAILED = list()
                    .defaults(
                            "&c&lFailed to remove! &fThere are some errors, please see console for details."
                    ).params("amount", "time").build();
        }


    }

    public static class MIGRATE extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> START = list()
                .defaults(
                        "&fMigrating the whitelist data form the whitelist.json ...",
                        "&fThis may take a while, please wait patiently."
                ).build();

        public static final ConfiguredMessageList<BaseComponent[]> SUCCESS = list()
                .defaults(
                        "&a&lSuccess! &There are %(amount) users added to whitelist, cost &a%(time)&fms."
                ).params("amount", "time").build();

        public static final ConfiguredMessageList<BaseComponent[]> FAILED = list()
                .defaults(
                        "&c&lFailed! &cThere are some errors, please see console for details."
                ).params("amount", "time").build();

    }

    public static class SYNC extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> START = list()
                .defaults(
                        "&fSynchronizing the whitelist data with the server...",
                        "&fThis may take a while, please wait patiently."
                ).build();

        public static final ConfiguredMessageList<BaseComponent[]> SUCCESS = list()
                .defaults(
                        "&a&lSuccess! &There are %(amount) whitelist requests have been synchronized to the server, cost &a%(time)&fms."
                ).params("amount", "time").build();

    }

    public static class ABSTAIN extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> ABSTAINED = list()
                .defaults(
                        "&fYou have &c&lABSTAINED &fall future votes.",
                        "&fThat you will not be calculated in server's whitelist review results.",
                        "&7&o If you want to participate in server management as a member of the server and exercising your voting rights," +
                                " you can simply type &e&l/votepass abstain &7&oto toggle your abstain status."
                ).build();

        public static final ConfiguredMessageList<BaseComponent[]> PARTICIPATING = list()
                .defaults("&fYou can participate in all future votes now.").build();

    }

    public static class VOTE extends ConfigurationRoot {

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
                                " you can simply type &e&l/votepass abstain &7&oto abstain all the requests in the future."
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
