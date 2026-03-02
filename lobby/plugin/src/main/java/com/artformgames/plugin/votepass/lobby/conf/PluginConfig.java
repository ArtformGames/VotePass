package com.artformgames.plugin.votepass.lobby.conf;


import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import org.bukkit.Material;

@ConfigPath(root = true)
public interface PluginConfig extends Configuration {

    interface SERVERS extends Configuration {

        ConfiguredValue<String> FOLDER_NAME = ConfiguredValue.of(String.class, "servers");

        ConfiguredList<String> DISABLED = ConfiguredList.of("YOUR-DISABLED-SERVER-ID");

    }

    interface RULES extends Configuration {

        interface BOOK extends Configuration {

            ConfiguredMessage<String> ACCEPT = ConfiguredMessage.asString()
                    .defaults("[&a&l[ACCEPT THE RULE]](hover=&fClick to &a&lconfirm and accept the server rules&f. run_command=/votepass accept %(server))")
                    .params("server")
                    .build();

            ConfiguredMessage<String> DENY = ConfiguredMessage.asString()
                    .defaults("[&c&l[DENY THE RULE]](hover=&fClick to &c&lrefuse the server rules&f. run_command=/votepass deny %(server))")
                    .params("server")
                    .build();
        }

    }


    interface ANSWERING extends Configuration {

        ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lRequest &8| &f%(server)")
                .params("server")
                .build();


        interface BOOK_ITEM extends Configuration {

            @HeaderComments("Which slot should we put the answering book in players inventory? (0~8)")
            ConfiguredValue<Integer> SLOT = ConfiguredValue.of(Integer.class, 8);

            ConfiguredMessage<String> NAME = ConfiguredMessage.asString()
                    .defaults("&c&lAnswering book &8(Right click)")
                    .build();

            ConfiguredMessage<String> LORE = ConfiguredMessage.asString()
                    .defaults("&fOpen and answer your questions!")
                    .build();

        }

        interface ITEMS extends Configuration {

            ConfiguredItem REQUIRED = ConfiguredItem.create()
                    .defaultType(Material.WRITABLE_BOOK)
                    .defaultName("&8Q: &f%(title)")
                    .defaultLore(
                            " ",
                            "#description#",
                            " ",
                            "&e&l● &eClick and answer!"
                    ).params("title", "description").build();

            ConfiguredItem FINISHED = ConfiguredItem.create()
                    .defaultType(Material.WRITTEN_BOOK)
                    .defaultName("&8Q: &f%(title)")
                    .defaultLore(
                            " ",
                            "#description#",
                            " ",
                            "&a&l✔ &aAnswer finished.",
                            "&7&oClick to edit your answer."
                    ).params("title").build();


            ConfiguredItem CONFIRM = ConfiguredItem.create()
                    .defaultType(Material.EMERALD)
                    .defaultName("&a&lConfirm and submit the request")
                    .defaultLore(" ", "&fClick to confirm and submit your request.")
                    .build();

            ConfiguredItem CANCEL = ConfiguredItem.create()
                    .defaultType(Material.REDSTONE)
                    .defaultName("&c&lCancel the request")
                    .defaultLore(" ", "&fClick to cancel your request.")
                    .build();

            ConfiguredItem PENDING = ConfiguredItem.create()
                    .defaultType(Material.BARRIER)
                    .defaultName("&e&lPlease answer all the questions")
                    .defaultLore(
                            " ",
                            "&fAfter answered all the questions,",
                            "&fthat you can submit your request to the server users."
                    ).build();

        }
    }

    interface RESUBMIT extends Configuration {

        ConfiguredMessage<String> TITLE = ConfiguredMessage.asString()
                .defaults("&a&lRequest &8| &f%(server)")
                .params("server")
                .build();

        interface ITEMS extends Configuration {

            ConfiguredItem INFO = ConfiguredItem.create()
                    .defaultType(Material.NAME_TAG)
                    .defaultName("&ePrevious request &8#&f%(id)")
                    .defaultLore(
                            " ",
                            "&fWe found a previous request with",
                            "  &a Approved %(approve)",
                            "  &c Rejected %(reject)",
                            "  &e Abstained %(abstain)",
                            "&7&oMaybe the request wasn't rejected, ",
                            "&7&obut simply closed automatically because it took too long.",
                            " ",
                            "&e Would you like to modify your previous request",
                            "&e and resubmit it for the next round of voting?",
                            " "
                    ).params("id", "approve", "reject", "abstain").build();

            ConfiguredItem ANSWERS = ConfiguredItem.create()
                    .defaultType(Material.WRITTEN_BOOK)
                    .defaultName("&8Q: &f%(question)")
                    .defaultLore(
                            " ",
                            "#answers#",
                            " "
                    ).params("id", "question", "answers").build();

            ConfiguredItem CONFIRM = ConfiguredItem.create()
                    .defaultType(Material.EMERALD)
                    .defaultName("&a&lYes, I'd like to modify previous request.")
                    .defaultLore(" ", "&fClick to confirm and modify your previous request.")
                    .build();

            ConfiguredItem CANCEL = ConfiguredItem.create()
                    .defaultType(Material.REDSTONE)
                    .defaultName("&c&lNope, create a new request.")
                    .defaultLore(" ", "&fClick to create your new request.")
                    .build();

        }
    }

}
