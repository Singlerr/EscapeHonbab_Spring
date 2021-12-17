package io.github.escapehonbab.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.escapehonbab.jpa.objects.User;

public interface MatchingHandlerCallback {

    void onMatched(MatchingUserWrapper wrapper, User target) throws JsonProcessingException, InterruptedException;
}
