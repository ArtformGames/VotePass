package com.artformgames.plugin.votepass.game.whitelist;

import com.artformgames.plugin.votepass.api.user.UserKey;
import com.artformgames.plugin.votepass.core.database.DataTables;
import com.artformgames.plugin.votepass.game.Main;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistModifier;
import com.artformgames.plugin.votepass.game.api.whiteist.WhitelistUserModifier;
import com.artformgames.plugin.votepass.game.user.UsersManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WhitelistModifierImpl implements WhitelistModifier {

    Set<WhitelistUserModifierImpl> users = new HashSet<>();
    Set<UserKey> removed = new HashSet<>();

    @Override
    public @NotNull CompletableFuture<Integer> execute() {
        UsersManager manager = Main.getInstance().getUserManager();
        String server = Main.getInstance().getServerID();

        Set<WhitelistUserModifierImpl> modifiers = new HashSet<>(this.users);
        modifiers.removeIf(u -> removed.contains(u.key));

        List<Object[]> params = new ArrayList<>();
        for (WhitelistUserModifierImpl modifier : modifiers) {
            WhitelistUser user = modifier.toUser();
            manager.addWhitelist(user);
            params.add(new Object[]{server, user.getUID(), user.isAbstained() ? 1 : 0, user.getPassedTime(), user.getLastOnline()});
        }

        CompletableFuture<Integer> replace = DataTables.VOTES.createReplaceBatch()
                .setColumnNames("server", "user", "request", "abstained", "passed_time", "online_time")
                .setAllParams(params)
                .executeFuture(l -> l.stream().mapToInt(Integer::intValue).sum());

        CompletableFuture<Integer> delete = new CompletableFuture<>();

        for (UserKey key : removed) {
            if (!manager.removeWhitelist(key)) continue;

            delete = delete.thenCombine(
                    DataTables.VOTES.createDelete()
                            .addCondition("server", server)
                            .addCondition("user", key.id())
                            .setLimit(1).build().executeFuture(l -> l),
                    Integer::sum
            );
        }

        return replace.thenCombine(delete, Integer::sum);
    }

    @Override
    public WhitelistModifierImpl modify(UserKey user, @Nullable Consumer<WhitelistUserModifier> migrator) {
        WhitelistUserModifierImpl impl = new WhitelistUserModifierImpl(user);
        if (migrator != null) migrator.accept(impl);
        users.add(impl);
        return this;
    }

    @Override
    public WhitelistModifierImpl remove(UserKey user) {
        this.removed.add(user);
        return this;
    }

}
