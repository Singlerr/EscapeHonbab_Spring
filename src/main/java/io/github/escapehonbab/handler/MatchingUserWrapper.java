package io.github.escapehonbab.handler;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.jpa.objects.DesiredTarget;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MatchingUserWrapper {
    private final DesiredTarget user;
    private final MatchingHandlerCallback callback;
    @Setter
    private UserMatchingState state;

    private MatchingUserWrapper(DesiredTarget user, MatchingHandlerCallback callback) {
        this.user = user;
        this.callback = callback;
    }

    public static MatchingUserWrapper getInstance(DesiredTarget user, MatchingHandlerCallback callback) {
        return new MatchingUserWrapper(user, callback);
    }
}
