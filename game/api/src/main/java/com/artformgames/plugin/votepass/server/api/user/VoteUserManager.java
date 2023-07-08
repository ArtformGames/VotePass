package com.artformgames.plugin.votepass.server.api.user;

import com.artformgames.plugin.votepass.api.user.UserDataManager;
import org.bukkit.entity.Player;

public interface VoteUserManager extends UserDataManager<VoteUserData> {

    boolean isAdmin(Player player);

}
