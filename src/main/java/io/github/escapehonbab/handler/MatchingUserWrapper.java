package io.github.escapehonbab.handler;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.jpa.objects.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MatchingUserWrapper {
    private final User user;
    private final MatchingHandlerCallback callback;
    @Setter
    private UserMatchingState state;

    private MatchingUserWrapper(User user, MatchingHandlerCallback callback) {
        this.user = user;
        this.callback = callback;
    }

    public static MatchingUserWrapper getInstance(User user, MatchingHandlerCallback callback) {
        return new MatchingUserWrapper(user, callback);
    }
}
