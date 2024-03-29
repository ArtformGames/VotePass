package com.artformgames.plugin.votepass.core.database;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.builder.TableQueryBuilder;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.api.data.vote.VoteDecision;
import com.artformgames.plugin.votepass.api.data.vote.VoteInformation;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.VotePassPlugin;
import com.artformgames.plugin.votepass.core.user.AbstractUserManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DataManager {

    protected final VotePassPlugin plugin;
    protected SQLManager sqlManager;

    public DataManager(VotePassPlugin plugin) {
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

    public AbstractUserManager<?> getUserManager() {
        return plugin.getUserManager();
    }

    public Set<String> getUserPassedServers(long uid) throws SQLException {
        return DataTables.LIST.createQuery()
                .selectColumns("server")
                .addCondition("user", uid)
                .build().executeFunction(query -> {
                    Set<String> servers = new HashSet<>();
                    ResultSet resultSet = query.getResultSet();
                    while (resultSet.next()) {
                        servers.add(resultSet.getString("server"));
                    }
                    return servers;
                }, new HashSet<>());
    }

    public CompletableFuture<Boolean> updateRequest(@NotNull RequestInformation request) {
        return DataTables.REQUESTS.createUpdate().setColumnValues(
                new String[]{"assignee", "result", "feedback", "closed_time"},
                new Object[]{
                        request.getAssignee() == null ? null : request.getAssignee().id(),
                        request.getResult().getID(), request.isFeedback() ? 1 : 0, request.getCloseTime()
                }
        ).addCondition("id", request.getID()).build().executeFuture(l -> l > 0);
    }

    public @NotNull Map<Integer, RequestInformation> queryRequests(@Nullable Consumer<@NotNull TableQueryBuilder> conditions) throws SQLException {
        TableQueryBuilder builder = DataTables.REQUESTS.createQuery();
        if (conditions != null) conditions.accept(builder);
        return builder.build().executeFunction(query -> readRequests(query.getResultSet()), new LinkedHashMap<>());
    }

    private @NotNull RequestInformation readRequest(ResultSet rs) throws SQLException {
        UserKey user = getUserManager().getKey(rs.getLong("user"));
        if (user == null) throw new SQLException("User not found: #" + rs.getLong("user"));
        return new RequestInformation(
                rs.getInt("id"), rs.getString("server"), user,
                DataSerializer.deserializeAnswers(rs.getString("contents")),
                loadRequestVotes(rs.getInt("id")),
                Optional.ofNullable(rs.getString("assignee")).map(getUserManager()::getKey).orElse(null),
                RequestResult.parse(rs.getInt("result")),
                rs.getBoolean("feedback"),
                rs.getTimestamp("create_time").toLocalDateTime(),
                Optional.ofNullable(rs.getTimestamp("closed_time")).map(Timestamp::toLocalDateTime).orElse(null)
        );
    }

    private @NotNull Map<Integer, RequestInformation> readRequests(ResultSet resultSet) throws SQLException {
        Map<Integer, RequestInformation> requests = new LinkedHashMap<>();
        while (resultSet.next()) {
            int requestID = resultSet.getInt("id");
            try {
                requests.put(requestID, readRequest(resultSet));
            } catch (Exception ex) {
                getLogger().severe("Error occurred when reading #" + requestID + " ：" + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return requests;
    }


    public @NotNull Set<VoteInformation> loadRequestVotes(int id) throws SQLException {
        return DataTables.VOTES.createQuery()
                .addCondition("request", id)
                .build().executeFunction(query -> {
                    Set<VoteInformation> votes = new HashSet<>();
                    while (query.getResultSet().next()) {
                        votes.add(readVote(query.getResultSet()));
                    }
                    return votes;
                }, new HashSet<>());
    }

    private @NotNull VoteInformation readVote(ResultSet rs) throws SQLException {
        UserKey user = getUserManager().getKey(rs.getLong("voter"));
        if (user == null) throw new SQLException("User not found: #" + rs.getLong("voter"));

        return new VoteInformation(
                rs.getInt("request"), user,
                VoteDecision.parse(rs.getInt("decision")),
                rs.getString("comment"),
                rs.getTimestamp("time").toLocalDateTime()
        );
    }
}
