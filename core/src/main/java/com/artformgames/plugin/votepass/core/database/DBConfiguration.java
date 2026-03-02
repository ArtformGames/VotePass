package com.artformgames.plugin.votepass.core.database;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@HeaderComments({"", "Database Configuration", "  Used to provide database connection for data operation."})
public class DBConfiguration implements Configuration {

    @ConfigPath("driver")
    protected static final ConfiguredValue<String> DRIVER_NAME = ConfiguredValue.of(
            String.class, "com.mysql.cj.jdbc.Driver"
    );

    protected static final ConfiguredValue<String> HOST = ConfiguredValue.of(String.class, "127.0.0.1");
    protected static final ConfiguredValue<Integer> PORT = ConfiguredValue.of(Integer.class, 3306);
    protected static final ConfiguredValue<String> DATABASE = ConfiguredValue.of(String.class, "minecraft");
    protected static final ConfiguredValue<String> USERNAME = ConfiguredValue.of(String.class, "root");
    protected static final ConfiguredValue<String> PASSWORD = ConfiguredValue.of(String.class, "password");
    protected static final ConfiguredValue<String> EXTRA = ConfiguredValue.of(String.class, "?useSSL=false");

    public static final ConfiguredValue<String> TABLE_PREFIX = ConfiguredValue.of(String.class, "VotePass_".toLowerCase());

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s", HOST.get(), PORT.get(), DATABASE.get(), EXTRA.get());
    }

}
