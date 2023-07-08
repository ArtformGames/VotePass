package com.artformgames.plugin.votepass.core.database;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.ForeignKeyRule;
import cc.carm.lib.easysql.api.enums.IndexType;
import com.artformgames.plugin.votepass.api.user.UserKeyManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public enum DataTables implements SQLTable {

    USERS("users", table -> {
        table.addAutoIncrementColumn("id");
        table.addColumn(UserKeyManager.KeyType.UUID.getColumnName(), "CHAR(36) NOT NULL");
        table.addColumn(UserKeyManager.KeyType.NAME.getColumnName(), "VARCHAR(16) NOT NULL");
    }),

    REQUESTS("requests", table -> {
        table.addAutoIncrementColumn("id");
        table.addColumn("server", "VARCHAR(24)");
        table.addColumn("user", "INT UNSIGNED NOT NULL");

        table.addColumn("contents", "LONGTEXT"); // Request answers
        table.addColumn("result", "TINYINT UNSIGNED NOT NULL DEFAULT 0");
        table.addColumn("feedback", "BIT NOT NULL DEFAULT 0");

        // The operator who handled this request.
        table.addColumn("assignee", "INT UNSIGNED");

        table.addColumn("create_ime", "DATETIME NOT NULL");
        table.addColumn("closed_ime", "DATETIME");

        table.setIndex(IndexType.INDEX, "idx_votepass_request", "server", "user");
        table.setIndex(IndexType.INDEX, "idx_votepass_request_user", "user");
        table.addForeignKey(
                "user", "fk_votepass_request_user",
                DataTables.USERS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
    }),

    VOTES("votes", table -> {
        table.addColumn("request", "INT UNSIGNED NOT NULL");
        table.addColumn("voter", "INT UNSIGNED NOT NULL");
        table.addColumn("decision", "TINYINT UNSIGNED NOT NULL DEFAULT 0");
        table.addColumn("comment", "TEXT");
        table.addColumn("time", "DATETIME NOT NULL");

        table.setIndex(IndexType.PRIMARY_KEY, "pk_votepass_vote", "request", "voter");
        table.setIndex(IndexType.INDEX, "uk_votepass_request", "request");
        table.setIndex(IndexType.INDEX, "uk_votepass_voter", "voter");

        table.addForeignKey(
                "voter", "fk_votepass_vote_user",
                DataTables.USERS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
        table.addForeignKey(
                "request", "fk_votepass_vote_request",
                DataTables.REQUESTS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
    }),

    LIST("list", table -> {
        table.addColumn("server", "VARCHAR(24) NOT NULL");
        table.addColumn("user", "INT UNSIGNED NOT NULL");
        table.addColumn("request", "INT UNSIGNED");

        // Whether this user are abstained from voting (Will not be count in active users)
        table.addColumn("abstain", "BIT NOT NULL DEFAULT 0");

        // The time when the user was added to the whitelist
        table.addColumn("passed_time", "DATETIME NOT NULL");
        // The time when the user was last online
        table.addColumn("online_time", "DATETIME");

        table.setIndex(IndexType.PRIMARY_KEY, "pk_votepass_list", "server", "user");
        table.setIndex(IndexType.INDEX, "idx_votepass_list_user", "user");
        table.addForeignKey(
                "user", "fk_votepass_list_user",
                DataTables.USERS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
        table.addForeignKey(
                "request", "fk_votepass_list_request",
                DataTables.REQUESTS.getTableName(), "id",
                ForeignKeyRule.CASCADE, ForeignKeyRule.CASCADE
        );
    });

    private final @Nullable String tableName;
    private final @Nullable Consumer<TableCreateBuilder> builder;
    private SQLManager manager;


    DataTables(@Nullable String tableName,
               @Nullable Consumer<TableCreateBuilder> builder) {
        this.tableName = tableName;
        this.builder = builder;
    }

    public boolean create(@NotNull SQLManager sqlManager) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    public @NotNull String getTableName() {
        String prefix = DBConfiguration.TABLE_PREFIX.getNotNull();
        String name = Optional.ofNullable(this.tableName).orElse(name().toLowerCase());
        return prefix + name;
    }
}
