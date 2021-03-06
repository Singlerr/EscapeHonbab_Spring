package io.github.escapehonbab.handler.pool;

import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.handler.UserScoreCalculator;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class MatchingPool {
    private static MatchingPool instance;
    private final List<MatchingUserWrapper> userQueue;
    private final UserScoreCalculator userScoreCalculator;

    private MatchingPool() {
        this.userQueue = new ArrayList<>();
        this.userScoreCalculator = UserScoreCalculator.getInstance();
    }

    public static MatchingPool getInstance() {
        if (instance == null)
            return (instance = new MatchingPool());

        return instance;
    }

    public void addQueue(MatchingUserWrapper user) {
        MatchingTask task = new MatchingTask(user, new ArrayList<>(userQueue), userScoreCalculator, this);
        task.start();
        this.userQueue.add(user);
    }

    public void removeIfExists(MatchingUserWrapper user) {
        userQueue.removeIf(e -> e.getUser().getUser().getUserId().equals(user.getUser().getUser().getUserId()));
    }

    public class MatchingTask extends Thread {


        private final List<MatchingUserWrapper> userQueue;
        private final MatchingUserWrapper user;
        private final UserScoreCalculator calculator;
        private final MatchingPool pool;

        public MatchingTask(MatchingUserWrapper user, List<MatchingUserWrapper> userQueue, UserScoreCalculator calculator, MatchingPool pool) {
            this.user = user;
            this.userQueue = userQueue;
            this.calculator = calculator;
            this.pool = pool;
            calculator.setDesiredTarget(user.getUser());
        }

        @SneakyThrows
        @Override
        public void run() {
            int bestUserIndex = 0;
            double bestScore = 0;
            if (userQueue.size() == 0)
                return;

            for (int i = 0; i < userQueue.size(); i++) {
                //TODO("하한값을 두어 최소 상대방이 갖추어야 할 조건을 설정해야함. 하한값을 두지 않을 경우, 거리가 엄청 먼데도 상대로 잡히는 경우가 존재")
                calculator.setTargetUser(userQueue.get(i).getUser());
                if (calculator.getTotalScore() > bestScore) {
                    bestScore = calculator.getTotalScore();
                    bestUserIndex = i;
                }
            }
            MatchingUserWrapper matched = userQueue.get(bestUserIndex);
            user.getCallback().onMatched(user, matched.getUser().getUser());
            matched.getCallback().onMatched(matched, user.getUser().getUser());
            pool.removeIfExists(user);
            pool.removeIfExists(matched);
            interrupt();
        }
    }
}
