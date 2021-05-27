package io.github.escapehonbab.handler.pool;

import io.github.escapehonbab.handler.MatchingUserWrapper;
import io.github.escapehonbab.handler.UserScoreCalculator;
import io.github.escapehonbab.jpa.objects.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MatchingPool {
    private List<MatchingUserWrapper> userQueue;
    private UserScoreCalculator userScoreCalculator;

    public MatchingPool(){
        this.userQueue = new ArrayList<>();
        this.userScoreCalculator = new UserScoreCalculator();
    }
    public void addQueue(MatchingUserWrapper user){
        MatchingTask task = new MatchingTask(user,new ArrayList<>(userQueue),userScoreCalculator);
        task.start();
        this.userQueue.add(user);
    }
    public class MatchingTask extends Thread{


        private MatchingUserWrapper user;
        private List<MatchingUserWrapper> userQueue;
        private UserScoreCalculator calculator;
        public MatchingTask(MatchingUserWrapper user,List<MatchingUserWrapper> userQueue,UserScoreCalculator calculator){
            this.user = user;
            this.userQueue = userQueue;
            this.calculator = calculator;
            calculator.setUser1(user.getUser());
        }
        @Override
        public void run(){
            int bestUserIndex = 0;
            double bestScore = 0;
            for(int i = 0;i<userQueue.size();i++){
                //TODO("하한값을 두어 최소 상대방이 갖추어야 할 조건을 설정해야함. 하한값을 두지 않을 경우, 거리가 엄청 먼데도 상대로 잡히는 경우가 존재")
                calculator.setUser2(userQueue.get(i).getUser());
                if(calculator.getTotalScore() > bestScore){
                    bestScore = calculator.getTotalScore();
                    bestUserIndex = i;
                }
            }
            MatchingUserWrapper matched = userQueue.get(bestUserIndex);
            user.getCallback().onMatched(matched.getUser());
            matched.getCallback().onMatched(user.getUser());
        }
    }
}
