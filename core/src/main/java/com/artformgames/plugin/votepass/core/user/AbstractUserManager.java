package com.artformgames.plugin.votepass.core.user;

import cc.carm.lib.easyplugin.EasyPlugin;
import com.artformgames.plugin.votepass.api.user.UserDataManager;
import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.database.DataTables;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractUserManager<U extends AbstractUserData> implements UserDataManager<U> {

    protected final @NotNull EasyPlugin plugin;

    protected final @NotNull ExecutorService executor;
    protected final @NotNull Map<UUID, U> dataCache;

    protected AbstractUserManager(@NotNull EasyPlugin plugin) {
        this.plugin = plugin;
        this.executor = Executors.newCachedThreadPool((r) -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName(plugin.getName() + "-UserManager");
            return t;
        });
        this.dataCache = new HashMap<>();
    }

    public abstract @NotNull U empty(@NotNull UserKey key);

    protected abstract @Nullable U loadData(@NotNull UserKey key) throws Exception;

    protected abstract void saveData(@NotNull U data) throws Exception;

    public void shutdown() {
        this.executor.shutdown();
    }

    protected @NotNull EasyPlugin getPlugin() {
        return plugin;
    }

    protected @NotNull Logger getLogger() {
        return getPlugin().getLogger();
    }

    public @NotNull Map<UUID, U> getDataCache() {
        return dataCache;
    }

    @Override
    public @Nullable UserKey getKey(KeyType type, Object param) {
        UserKey fromCache = getDataCache().values().stream()
                .filter(user -> user.getKey().equals(type, param))
                .findFirst().map(U::getKey).orElse(null);
        if (fromCache != null) return fromCache;

        return DataTables.USERS.createQuery()
                .addCondition(type.name().toLowerCase(), param)
                .setLimit(1).build().execute(query -> {
                    ResultSet resultSet = query.getResultSet();
                    if (!resultSet.next()) return null;
                    return new UserKey(
                            resultSet.getInt("id"),
                            UUID.fromString(resultSet.getString(KeyType.UUID.getColumnName())),
                            resultSet.getString(KeyType.NAME.getColumnName())
                    );
                }, null, null);
    }

    @Override
    public @NotNull UserKey upsertKey(@NotNull UUID uuid, @Nullable String username) throws Exception {
        UserKey exists = getKey(KeyType.UUID, uuid);
        if (exists != null) return exists;

        long uid = DataTables.USERS.createInsert()
                .setColumnNames(KeyType.UUID.getColumnName(), KeyType.NAME.getColumnName())
                .setParams(uuid.toString(), username)
                .returnGeneratedKey(Long.class)
                .executeFunction(l -> l, -1L);

        if (uid <= 0) throw new Exception("Failed to insert user into database");
        return new UserKey(uid, uuid, username);
    }

    @Override
    public @Unmodifiable @NotNull Set<U> list() {
        return Set.copyOf(getDataCache().values());
    }

    @Override
    public @Nullable U getNullable(@NotNull UUID uuid) {
        return getDataCache().get(uuid);
    }

    @Override
    public @NotNull CompletableFuture<U> load(@NotNull UUID uuid, @NotNull BooleanSupplier cacheCondition) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return upsertKey(uuid, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor).thenCompose(data -> load(data, cacheCondition));
    }

    @Override
    public @NotNull CompletableFuture<U> load(@NotNull UserKey key, @NotNull BooleanSupplier cacheCondition) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                long s1 = System.currentTimeMillis();
                getPlugin().debug("Loading " + key.name() + "(#" + key.id() + ")" + "'s data...");
                U data = loadData(key);
                if (data == null) {
                    getPlugin().debug("Creating a empty data for " + key.name() + "(#" + key.id() + ")" + "...");
                    return empty(key);
                } else {
                    getPlugin().debug("Load " + key.name() + "(#" + key.id() + ")" + "'s data finished, cost" + (System.currentTimeMillis() - s1) + " ms.");
                    return data;
                }
            } catch (Exception ex) {
                getPlugin().error("Load " + key.name() + "(#" + key.id() + ")" + " Failed, please check the configuration!");
                ex.printStackTrace();
                return empty(key);
            }
        }, executor).thenApply(data -> {
            if (cacheCondition.getAsBoolean()) dataCache.put(key.uuid(), data);
            return data;
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> save(@NotNull U user) {
        return CompletableFuture.supplyAsync(() -> {
            UserKey key = user.getKey();
            try {
                long s1 = System.currentTimeMillis();
                getPlugin().debug("Saving " + key.name() + "(#" + key.id() + ")" + "'s data...");
                saveData(user);
                getPlugin().debug("Save " + key.name() + "(#" + key.id() + ")" + "'s data finished, cost " + (System.currentTimeMillis() - s1) + " ms.");
                return true;
            } catch (Exception ex) {
                getPlugin().error("Save " + key.name() + "(#" + key.id() + ")" + " failed, please check the configurations.");
                ex.printStackTrace();
                return false;
            }
        }, executor);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> unload(@NotNull UUID key, boolean save) {
        U data = getNullable(key);
        if (data == null) return CompletableFuture.completedFuture(false);

        if (save) {
            return save(data).thenApply(result -> {
                this.dataCache.remove(key);
                return result;
            });
        } else {
            this.dataCache.remove(key);
            return CompletableFuture.completedFuture(true);
        }

    }

    public @NotNull CompletableFuture<Map<UUID, U>> loadOnline(@NotNull Function<Player, UUID> function) {
        return loadGroup(Bukkit.getOnlinePlayers(), function, OfflinePlayer::isOnline);
    }

    public <T> @NotNull CompletableFuture<Map<UUID, U>> loadGroup(@NotNull Collection<? extends T> users,
                                                                  @NotNull Function<? super T, UUID> function,
                                                                  @NotNull Predicate<T> cacheCondition) {
        CompletableFuture<Map<UUID, U>> task = CompletableFuture.completedFuture(new ConcurrentHashMap<>());
        if (users.isEmpty()) return task;

        Map<UUID, T> usersMap = users.stream().collect(Collectors.toMap(function, Function.identity()));
        for (Map.Entry<UUID, T> entry : usersMap.entrySet()) {
            UUID key = entry.getKey();
            T user = entry.getValue();
            task = task.thenCombine(
                    load(key, () -> cacheCondition.test(user)),
                    (map, result) -> {
                        map.put(key, result);
                        return map;
                    }
            );
        }

        return task.thenApply(Collections::unmodifiableMap);
    }

    public @NotNull CompletableFuture<Map<UUID, U>> loadGroup(@NotNull Collection<UUID> allKeys,
                                                              @NotNull Predicate<UUID> cacheCondition) {
        return loadGroup(allKeys, Function.identity(), cacheCondition);
    }

    public @NotNull CompletableFuture<Map<UUID, U>> loadGroup(@NotNull Collection<UUID> allKeys) {
        return loadGroup(allKeys, (v) -> false);
    }

    public @NotNull CompletableFuture<Integer> saveAll() {
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(0);
        if (getDataCache().isEmpty()) return future;

        for (U value : getDataCache().values()) {
            future = future.thenCombine(save(value), (before, result) -> before + (result ? 1 : 0));
        }

        return future;
    }

}
