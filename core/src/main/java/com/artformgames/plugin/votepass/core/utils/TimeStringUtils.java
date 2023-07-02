package com.artformgames.plugin.votepass.core.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class TimeStringUtils {

    private TimeStringUtils() {
    }

    public static String serializeSeconds(long allSeconds) {
        long days = allSeconds / 86400L;
        long hours = allSeconds % 86400L / 3600L;
        long minutes = allSeconds % 3600L / 60L;
        long seconds = allSeconds % 60L;

        StringBuilder sb = new StringBuilder();
        if (days > 0L) sb.append(days).append("d,");
        if (hours > 0L) sb.append(hours).append("h,");
        if (minutes > 0L) sb.append(minutes).append("m,");
        if (seconds > 0L) sb.append(seconds).append("s");
        // if last char is "," remove it
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Contract("!null->!null;null->null")
    public static String serializeDuration(@Nullable Duration duration) {
        if (duration == null) return null;
        return serializeSeconds(duration.toSeconds());
    }

    private static long parse(String s) {
        String[] sl = s.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        long i = Long.parseLong(sl[0]);
        try {
            return switch (sl[1]) {
                case "s" -> (i);
                case "m" -> (i * 60);
                case "h" -> (i * 60 * 60);
                case "d" -> (i * 60 * 60 * 24);
                case "w" -> (i * 60 * 60 * 24 * 7);
                case "mo" -> (i * 60 * 60 * 24 * 30);
                case "y" -> (i * 60 * 60 * 24 * 30 * 12);
                default -> -1;
            };
        } catch (Exception e) {
            return -1;
        }
    }

    public static long parseSeconds(String s) {
        String[] ss = s.split(",");
        long l = 0;
        for (String temp : ss) {
            long value = parse(temp);
            if (value == -1) return -1;
            l += value;
        }
        return l;
    }

    public static @Nullable Duration parseDuration(String s) {
        long sec = parseSeconds(s);
        if (sec < 0) return null;
        return Duration.ofSeconds(sec);
    }

}
