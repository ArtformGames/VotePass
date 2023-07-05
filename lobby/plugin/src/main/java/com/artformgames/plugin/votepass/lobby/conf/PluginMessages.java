package com.artformgames.plugin.votepass.lobby.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import com.artformgames.plugin.votepass.core.conf.CommonMessages;
import com.artformgames.plugin.votepass.core.conf.TextMessages;
import net.md_5.bungee.api.chat.BaseComponent;

public class PluginMessages extends TextMessages {

    public static final Class<?> COMMON = CommonMessages.class;

    public static final ConfiguredMessageList<BaseComponent[]> NOT_EXISTS = list()
            .defaults(
                    "&fThere is no server configuration with ID &e%(server) &f, please check."
            ).params("server").build();

    public static final ConfiguredMessageList<BaseComponent[]> DISABLED = list()
            .defaults(
                    "&lSorry! &fHowever, the whitelist application for &e%(server) &f is not open yet, please refer to the server chat group for details."
            ).params("server").build();


    public static final ConfiguredMessageList<BaseComponent[]> RULES = list()
            .defaults(
                    "&fYou first need to &a understand and accept &6%(name) &f server rules, please read carefully to make sure you know it."
            ).params("name").build();

    public static final ConfiguredMessageList<BaseComponent[]> ACCEPTED = list()
            .defaults(
                    "&fYou HAVE &aknowledged and accepted the server rules of &6%(name) &f, and then you can fill in your application content."
            ).params("name").build();

    public static final ConfiguredMessageList<BaseComponent[]> REJECTED = list()
            .defaults(
                    "&c&lSorry! &fBut you have &c&lrejected server rules for &6%(name) &f, so you cannot join this server."
            ).params("name").build();

    public static final ConfiguredMessageList<BaseComponent[]> WHITELISTED = list()
            .defaults(
                    "&fYou already have the whitelist of &e%(name) &f, join the game now~"
            ).params("name").build();

    public static final ConfiguredMessageList<BaseComponent[]> PENDING = list()
            .defaults(
                    "&fYou already have a &6%(name) &f whitelist application with tracking number &e%(id) &f being reviewed, please wait patiently for the result~"
            ).params("id", "name").build();

    public static final ConfiguredMessageList<BaseComponent[]> WRITING = list()
            .defaults(
                    "&fWe placed a writable book in your NO.&e%(slot) &f slot for answering the question &r%(question) &f,",
                    "&fPlease right-click to open it and fill in the content you want to tell us, and click \"&a&lFinish&f\" directly after filling in to save."
            ).params("slot", "question").build();

    public static final ConfiguredMessageList<BaseComponent[]> CANCELLED = list()
            .defaults(
                    "&fYou have canceled your application for &6%(name) &f's white list, you can re-apply later."
            ).params("name").build();

    public static final ConfiguredMessageList<BaseComponent[]> POSTED = list()
            .defaults(
                    "&fWe has successfully submitted the &6%(name) &f whitelist application for you. ",
                    "&fThe application number is &e%(id) &f. Please wait patiently for the result."
            ).params("id", "name").build();

    public static final ConfiguredMessageList<BaseComponent[]> ERROR = list()
            .defaults(
                    "&fWe failed to submitted the application, please contact the administrator."
            ).params("name").build();

    public static class FEEDBACK extends ConfigurationRoot {

        public static final ConfiguredSound SOUND = ConfiguredSound.of("ENTITY_CHICKEN_EGG");

        public static final ConfiguredMessageList<BaseComponent[]> APPROVED = list()
                .defaults(
                        "&f Your application for &6%(name) &fserver whitelist &8#&e%(request_id)&f has been approved, welcome to join our family!"
                ).params("request_id", "name").build();

        public static final ConfiguredMessageList<BaseComponent[]> REJECTED = list()
                .defaults(
                        "&fYour application for &6%(name) &fserver whitelist &8#&e%(request_id)&f has been rejected, please check the application content and try again."
                ).params("request_id", "name").build();

    }

}
