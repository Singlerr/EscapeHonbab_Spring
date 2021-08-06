package io.github.escapehonbab.handler;

import io.github.escapehonbab.handler.pool.MatchingPool;

public class MatchingHandler {
    private static MatchingHandler instance;
    private final MatchingPool pool;

    private MatchingHandler() {
        this.pool = MatchingPool.getInstance();
    }

    public static MatchingHandler getInstance() {
        if (instance == null)
            return (instance = new MatchingHandler());
        return instance;
    }

    public void submit(MatchingUserWrapper wrapper) {
        pool.addQueue(wrapper);
    }


}
