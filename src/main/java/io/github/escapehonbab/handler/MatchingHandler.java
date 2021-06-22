package io.github.escapehonbab.handler;

import io.github.escapehonbab.handler.pool.MatchingPool;
import io.github.escapehonbab.jpa.objects.User;

public class MatchingHandler {
    private MatchingPool pool;

    private static MatchingHandler instance;

    private MatchingHandler(){
        this.pool = MatchingPool.getInstance();
    }

    public static MatchingHandler getInstance() {
        if(instance == null)
            return (instance = new MatchingHandler());
        return instance;
    }

    public void submit(MatchingUserWrapper wrapper){
        pool.addQueue(wrapper);
    }


}
