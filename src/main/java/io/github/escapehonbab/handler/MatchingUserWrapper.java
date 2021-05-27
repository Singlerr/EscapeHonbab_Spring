package io.github.escapehonbab.handler;

import io.github.escapehonbab.UserMatchingState;
import io.github.escapehonbab.jpa.objects.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MatchingUserWrapper {
    private User user;
    private MatchingHandlerCallback callback;

    @Setter
    private UserMatchingState state;
    public MatchingUserWrapper(User user, MatchingHandlerCallback callback){
        this.user = user;
        this.callback = callback;
    }
}
