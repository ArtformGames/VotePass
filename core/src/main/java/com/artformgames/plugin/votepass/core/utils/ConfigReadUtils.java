package com.artformgames.plugin.votepass.core.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class ConfigReadUtils {
    private ConfigReadUtils() {
    }

    public static <V> Map<String, V> readSectionMap(@Nullable ConfigurationSection section,
                                                    @NotNull Function<ConfigurationSection, V> valueCast) {
        return readSectionMap(section, s -> s, valueCast);
    }


    public static <K, V> Map<K, V> readSectionMap(@Nullable ConfigurationSection section,
                                                  @NotNull Function<@NotNull String, @Nullable K> keyCast,
                                                  @NotNull Function<ConfigurationSection, V> valueCast) {
        if (section == null) return new LinkedHashMap<>();
        Map<K, V> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            if (!section.isConfigurationSection(key)) continue;
            K finalKey = keyCast.apply(key);
            if (finalKey == null) continue;
            V finalValue = valueCast.apply(section.getConfigurationSection(key));
            if (finalValue != null) result.put(finalKey, finalValue);
        }
        return result;
    }

    public static <V> SortedMap<Integer, V> readSortedSection(@Nullable ConfigurationSection section,
                                                              @NotNull Function<ConfigurationSection, V> valueCast) {
        return new TreeMap<>(readSectionMap(section, ConfigReadUtils::parseInt, valueCast));
    }

    public static <V> SortedMap<Integer, V> readSortedListMap(@Nullable ConfigurationSection section,
                                                              @NotNull Function<@NotNull List<String>, V> valueCast) {
        return new TreeMap<>(readListMap(section, ConfigReadUtils::parseInt, valueCast));
    }

    public static <K, V> Map<K, V> readListMap(@Nullable ConfigurationSection section,
                                               @NotNull Function<@NotNull String, @Nullable K> keyCast,
                                               @NotNull Function<@NotNull List<String>, V> valueCast) {
        if (section == null) return new LinkedHashMap<>();
        Map<K, V> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            if (!section.isList(key)) continue;
            K finalKey = keyCast.apply(key);
            if (finalKey == null) continue;
            V finalValue = valueCast.apply(section.getStringList(key));
            if (finalValue != null) result.put(finalKey, finalValue);
        }
        return result;
    }

    public static @Nullable Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception ex) {
            return null;
        }
    }
}
