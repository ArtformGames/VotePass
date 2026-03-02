package com.artformgames.plugin.votepass.lobby.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import net.md_5.bungee.api.chat.BaseComponent;

@ConfigPath(root = true)
public interface PluginMessages extends TextMessages {

    interface COMMAND extends Configuration {

        ConfiguredMessage<BaseComponent[]> USER =TextMessages.create()
                .defaults(
                        "&e&lVotePass &fCommands &7(/votepass)",
                        "&8#&f request &e<serverID>",
                        "&8-&7 Submit a whitelist request for a specific server."
                ).build();

        ConfiguredMessage<BaseComponent[]> ADMIN = TextMessages.create()
                .defaults(
                        "&8#&f toggle <server>",
                        "&8-&7 Enable/Disable the vote application for the corresponding server.",
                        "&8#&f reload",
                        "&8-&7 Reload the configuration file."
                ).build();
    }

    ConfiguredMessage<BaseComponent[]> NOT_EXISTS = TextMessages.create()
            .defaults(
                    "&fThere is no server configuration with ID &e%(server) &f, please check."
            ).params("server").build();

    ConfiguredMessage<BaseComponent[]> DISABLED = TextMessages.create()
            .defaults(
                    "&lSorry! &fHowever, the whitelist application for &e%(server) &f is not open yet, please refer to the server chat group for details."
            ).params("server").build();


    ConfiguredMessage<BaseComponent[]> RULES = TextMessages.create()
            .defaults(
                    "&fYou first need to &a understand and accept &6%(name) &f server rules, please read carefully to make sure you know it."
            ).params("name").build();

    ConfiguredMessage<BaseComponent[]> ACCEPTED = TextMessages.create()
            .defaults(
                    "&fYou HAVE &aknowledged and accepted the server rules of &6%(name) &f, and then you can fill in your application content."
            ).params("name").build();

    ConfiguredMessage<BaseComponent[]> REJECTED = TextMessages.create()
            .defaults(
                    "&c&lSorry! &fBut you have &c&lrejected server rules for &6%(name) &f, so you cannot join this server."
            ).params("name").build();

    ConfiguredMessage<BaseComponent[]> WHITELISTED = TextMessages.create()
            .defaults(
                    "&fYou already have the whitelist of &e%(name) &f, join the game now~"
            ).params("name").build();

    ConfiguredMessage<BaseComponent[]> PENDING = TextMessages.create()
            .defaults(
                    "&fYou already have a &6%(name) &f whitelist application with tracking number &e%(id) &f being reviewed, please wait patiently for the result~"
            ).params("id", "name").build();

    ConfiguredMessage<BaseComponent[]> WRITING = TextMessages.create()
            .defaults(
                    "&fWe placed a writable book in your NO.&e%(slot) &f slot for answering the question &r%(question) &f,",
                    "&fPlease right-click to open it and fill in the content you want to tell us, and click \"&a&lFinish&f\" directly after filling in to save."
            ).params("slot", "question").build();

    ConfiguredMessage<BaseComponent[]> CANCELLED = TextMessages.create()
            .defaults(
                    "&fYou have canceled your application for &6%(name) &f's white list, you can re-apply later."
            ).params("name").build();

    ConfiguredMessage<BaseComponent[]> POSTED = TextMessages.create()
            .defaults(
                    "&fWe has successfully submitted the &6%(name) &f whitelist application for you. ",
                    "&fThe application number is &e%(id) &f. Please wait patiently for the result."
            ).params("id", "name").build();

    ConfiguredMessage<BaseComponent[]> ERROR = TextMessages.create()
            .defaults(
                    "&fWe failed to submitted the application, please contact the administrator."
            ).params("name").build();

    interface FEEDBACK extends Configuration {

        ConfiguredSound SOUND = ConfiguredSound.of("ENTITY_CHICKEN_EGG");

        ConfiguredMessage<BaseComponent[]> APPROVED = TextMessages.create()
                .defaults(
                        "&f Your application for &6%(name) &fserver whitelist &8#&e%(request_id)&f has been approved, welcome to join our family!"
                ).params("request_id", "name").build();

        ConfiguredMessage<BaseComponent[]> REJECTED = TextMessages.create()
                .defaults(
                        "&fYour application for &6%(name) &fserver whitelist &8#&e%(request_id)&f has been rejected, please check the application content and try again."
                ).params("request_id", "name").build();

        ConfiguredMessage<BaseComponent[]> EXPIRED = TextMessages.create()
                .defaults(
                        "&fYour application for &6%(name) &fserver whitelist &8#&e%(request_id)&f has expired because no enough users to vote. please try again and call on more users to vote for you."
                ).params("request_id", "name").build();

    }

    interface TOGGLE extends Configuration {

        ConfiguredMessage<BaseComponent[]> DISABLED = TextMessages.create()
                .defaults(
                        "&fThe application for &6%(name) &fserver whitelist now has successfully &c&ldisabled&f!"
                ).params("name").build();

        ConfiguredMessage<BaseComponent[]> ENABLED = TextMessages.create()
                .defaults(
                        "&fThe application for &6%(name) &fserver whitelist now has successfully &a&lenabled &f!"
                ).params("name").build();

    }

}
