package com.artformgames.plugin.votepass.core.database;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;
import java.util.logging.Logger;

public class DataManager {

    protected final EasyPlugin plugin;
    protected SQLManager sqlManager;

    public DataManager(EasyPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() throws Exception {

        try {
            getLogger().info("Connecting to the database...");
            this.sqlManager = EasySQL.createManager(
                    DBConfiguration.DRIVER_NAME.getNotNull(), DBConfiguration.buildJDBC(),
                    DBConfiguration.USERNAME.getNotNull(), DBConfiguration.PASSWORD.getNotNull()
            );
            this.sqlManager.setDebugMode(() -> getPlugin().isDebugging());
        } catch (Exception exception) {
            throw new Exception("Unable to connect to the database, please check the configuration.", exception);
        }

        try {
            getLogger().info("Initializing database tables...");
            for (DataTables value : DataTables.values()) {
                value.create(this.sqlManager);
            }
        } catch (SQLException exception) {
            throw new Exception("A table required by the plugin could not be created, please check database permissions.", exception);
        }
    }

    public void shutdown() {
        getLogger().info("Shutting down database connections...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public EasyPlugin getPlugin() {
        return plugin;
    }

    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }

}
