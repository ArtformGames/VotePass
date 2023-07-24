package com.artformgames.plugin.votepass.lobby.server;

import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import com.artformgames.plugin.votepass.core.utils.ConfigReadUtils;
import com.artformgames.plugin.votepass.lobby.Main;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerQuestion;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerRules;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import com.artformgames.plugin.votepass.lobby.api.server.ServerSettingsManager;
import com.artformgames.plugin.votepass.lobby.conf.PluginConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.*;

public class SettingsManager implements ServerSettingsManager {

    private final JavaPlugin plugin;

    @Unmodifiable
    private @NotNull Map<String, ServerSettings> servers = Map.of();

    public SettingsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Unmodifiable
    public Map<String, ServerSettings> getServers() {
        return servers;
    }

    public @Nullable ServerSettings getSettings(@NotNull String serverID) {
        return servers.get(serverID);
    }

    public boolean isDisabled(@NotNull String serverID) {
        return PluginConfig.SERVERS.DISABLED.contains(serverID);
    }

    public void setDisabled(@NotNull String serverID, boolean disabled) {
        if (disabled) {
            if (!isDisabled(serverID)) PluginConfig.SERVERS.DISABLED.add(serverID);
        } else {
            PluginConfig.SERVERS.DISABLED.remove(serverID);
        }

        try {
            Main.getInstance().getConfiguration().save();
        } catch (Exception ignored) {
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    private File getStorageFolder() {
        return new File(getPlugin().getDataFolder(), getFolderName());
    }

    private String getFolderName() {
        return PluginConfig.SERVERS.FOLDER_NAME.getNotNull();
    }

    @Override
    public void reloadSettings() {
        File dataFolder = getStorageFolder();
        if (!dataFolder.isDirectory() || !dataFolder.exists()) {
            try {
                JarResourceUtils.copyFolderFromJar(
                        getFolderName(), getPlugin().getDataFolder(),
                        JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST
                );
            } catch (Exception ex) {
                boolean success = dataFolder.mkdirs();
            }
        }

        String[] filesList = dataFolder.list();
        if (filesList == null || filesList.length < 1) {
            Main.severe("   配置文件夹中暂无任何配置，请检查。");
            return;
        }

        List<File> files = Arrays.stream(filesList).map(s -> new File(dataFolder, s)).filter(File::isFile).toList();
        if (files.isEmpty()) return;

        HashMap<String, ServerSettings> dataConfigurations = new HashMap<>();

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith(".")) continue;
            try {
                ServerSettings config = parseApplication(file);
                Main.debugging(" Loaded server [#" + config.id() + "] " + config.name() + " (" + fileName + ")");
                dataConfigurations.put(config.id(), config);
            } catch (Exception ex) {
                Main.severe("Error occurred when loading [" + file.getAbsolutePath() + "], please check the configuration.");
                ex.printStackTrace();
            }
        }

        servers = Collections.unmodifiableMap(dataConfigurations);
    }

    @Override
    @Unmodifiable
    public @NotNull Set<ServerSettings> list() {
        return Set.copyOf(getServers().values());
    }

    public static @NotNull ServerSettings parseApplication(@NotNull File file) throws Exception {
        return parseApplication(YamlConfiguration.loadConfiguration(file));
    }

    public static @NotNull ServerSettings parseApplication(@NotNull FileConfiguration config) throws Exception {
        String identifier = config.getString("id");
        if (identifier == null) throw new Exception("identifier not provided.");
        return new ServerSettings(
                identifier, config.getString("name", identifier),
                parseRules(config.getConfigurationSection("rules")),
                parseQuestions(config.getConfigurationSection("questions"))
        );
    }

    public static @Nullable ServerRules parseRules(@Nullable ConfigurationSection section) {
        if (section == null) return null;

        SortedMap<Integer, List<String>> configPages = ConfigReadUtils.readSortedListMap(
                section.getConfigurationSection("pages"), l -> l
        );

        return new ServerRules(
                section.getString("title"),
                section.getString("author"),
                new ArrayList<>(configPages.values())
        );
    }

    public static @NotNull SortedMap<Integer, ServerQuestion> parseQuestions(@Nullable ConfigurationSection section) {
        if (section == null) return new TreeMap<>();
        return ConfigReadUtils.readSortedSection(section, SettingsManager::parseQuestion);
    }


    public static @Nullable ServerQuestion parseQuestion(@Nullable ConfigurationSection section) {
        if (section == null || (!section.contains("title") && !section.contains("description"))) return null;
        return new ServerQuestion(section.getString("title", " "), section.getStringList("description"));
    }

}
