package com.artformgames.plugin.votepass.game.api.whiteist;

import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public interface WhitelistUserModifier {

    default WhitelistUserModifier setLinkedRequest(@Nullable RequestInformation request) {
        return setLinkedRequestID(request == null ? null : request.getID());
    }

    WhitelistUserModifier setLinkedRequestID(Integer id);

    WhitelistUserModifier setAbstained(boolean abstained);

    WhitelistUserModifier setPassedTime(@NotNull LocalDateTime time);

    WhitelistUserModifier setLastOnline(@Nullable LocalDateTime time);

}
