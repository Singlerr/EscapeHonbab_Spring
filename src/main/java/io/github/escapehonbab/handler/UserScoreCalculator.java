package io.github.escapehonbab.handler;

import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.utils.GPSMath;
import lombok.Setter;


public class UserScoreCalculator {

    public static final double EQUAL_SEX = 1.5;
    public static final double UNEQUAL_SEX = 0.5;
    public static final double DISTANCE_WEIGHT = 0.5;
    /**
     * It must be multiplied to each of interests.
     */
    public static double EQUAL_INTEREST = 0.3;
    @Setter
    private User user1,user2;

    private UserScoreCalculator(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }

    private UserScoreCalculator(){}

    public static UserScoreCalculator getInstance(User user1,User user2){
        return new UserScoreCalculator(user1,user2);
    }

    public static UserScoreCalculator getInstance(){
        return new UserScoreCalculator();
    }


    public double getSexScore(){
        return user1.getSexType().equals(user2.getSexType()) ? EQUAL_SEX : UNEQUAL_SEX;
    }

    public double getDistanceScore(){
        return 0.5 * GPSMath.distance(user1.getGpsData().getLatitude(),user2.getGpsData().getLatitude(),user1.getGpsData().getLongitude(),user2.getGpsData().getLongitude(),'K');
    }

    public double getTotalScore(){
        return getSexScore() + getDistanceScore();
    }
}
