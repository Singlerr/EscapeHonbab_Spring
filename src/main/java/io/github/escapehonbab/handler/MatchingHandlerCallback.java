package io.github.escapehonbab.handler;

import io.github.escapehonbab.jpa.objects.User;

public interface MatchingHandlerCallback {

    void onMatched(MatchingUserWrapper wrapper, User target);
}