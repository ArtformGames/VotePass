package com.artformgames.plugin.votepass.lobby.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredItem;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import com.artformgames.plugin.votepass.core.conf.CommonConfig;
import org.bukkit.Material;

public class PluginConfig extends ConfigurationRoot {

    public static final Class<?> COMMON = CommonConfig.class;

    public static final class SERVERS extends ConfigurationRoot {

        public static final ConfiguredValue<String> FOLDER_NAME = ConfiguredValue.of(String.class, "servers");

        public static final ConfiguredList<String> DISABLED = ConfiguredList.of(String.class);

    }

    public static final class RULES extends ConfigurationRoot {

        public static final class BOOK extends ConfigurationRoot {

            public static final ConfiguredMessageList<String> ACCEPT = ConfiguredMessageList.asStrings()
                    .defaults("[&a&l[ACCEPT THE RULE]](hover=&fClick to &a&lconfirm and accept the server rules&f. run_command=/votepass accept %(server))")
                    .params("server")
                    .build();

            public static final ConfiguredMessageList<String> DENY = ConfiguredMessageList.asStrings()
                    .defaults("[&c&l[DENY THE RULE]](hover=&fClick to &c&lrefuse the server rules&f. run_command=/votepass deny %(server))")
                    .params("server")
                    .build();
        }

    }


    public static final class ANSWERING extends ConfigurationRoot {

        public static final ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lRequest &8| &f%(server)")
                .params("server")
                .build();


        public static final class BOOK_ITEM extends ConfigurationRoot {

            public static final ConfiguredValue<Integer> SLOT = ConfiguredValue.of(Integer.class, 8);

            public static final ConfiguredMessage<String> NAME = ConfiguredMessage.asString()
                    .defaults("&c&lAnswering book &8(Right click)")
                    .build();

            public static final ConfiguredMessageList<String> LORE = ConfiguredMessageList.asStrings()
                    .defaults("&fOpen and answer your questions!")
                    .build();

        }

        public static final class ITEMS extends ConfigurationRoot {

            public static final ConfiguredItem REQUIRED = ConfiguredItem.create()
                    .defaultType(Material.WRITABLE_BOOK)
                    .defaultName("&8Q: &f%(title)")
                    .defaultLore(
                            " ",
                            "#description#",
                            " ",
                            "&e&l● &eClick and answer!"
                    ).params("title", "description").build();

            public static final ConfiguredItem FINISHED = ConfiguredItem.create()
                    .defaultType(Material.WRITABLE_BOOK)
                    .defaultName("&8Q: &f%(title)")
                    .defaultLore(
                            " ",
                            "#description#",
                            " ",
                            "&a&l✔ &aAnswer finished.",
                            "&7&oClick to edit your answer."
                    ).params("title").build();


            public static final ConfiguredItem CONFIRM = ConfiguredItem.create()
                    .defaultType(Material.EMERALD)
                    .defaultName("&a&lConfirm and submit the request")
                    .defaultLore(" ", "&fClick to confirm and submit your request.")
                    .build();

            public static final ConfiguredItem CANCEL = ConfiguredItem.create()
                    .defaultType(Material.REDSTONE)
                    .defaultName("&c&lCancel the request")
                    .defaultLore(" ", "&fClick to cancel your request.")
                    .build();

            public static final ConfiguredItem PENDING = ConfiguredItem.create()
                    .defaultType(Material.BARRIER)
                    .defaultName("&e&lPlease answer all the questions")
                    .defaultLore(
                            " ",
                            "&fAfter answered all the questions,",
                            "&fthat you can submit your request to the server users."
                    ).build();

        }


    }

}
