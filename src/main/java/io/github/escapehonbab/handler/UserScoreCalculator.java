package io.github.escapehonbab.handler;

import io.github.escapehonbab.jpa.objects.DesiredTarget;
import io.github.escapehonbab.jpa.objects.SexScope;
import io.github.escapehonbab.utils.GPSMath;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;


public class UserScoreCalculator {

    public static final double EQUAL_SEX = 1.5;
    public static final double UNEQUAL_SEX = 0.5;
    public static final double DISTANCE_WEIGHT = 0.5;
    /**
     * It must be multiplied to each of interests.
     */
    public static double EQUAL_INTEREST = 0.3;
    @Setter
    private DesiredTarget desiredTarget;
    @Setter
    private DesiredTarget targetUser;

    private UserScoreCalculator(DesiredTarget user1, DesiredTarget user2) {
        this.desiredTarget = user1;
        this.targetUser = user2;
    }

    private UserScoreCalculator() {
    }

    public static UserScoreCalculator getInstance(DesiredTarget desiredTarget, DesiredTarget targetUser) {
        return new UserScoreCalculator(desiredTarget, targetUser);
    }

    public static UserScoreCalculator getInstance() {
        return new UserScoreCalculator();
    }


    public double getSexScore() {
        SexScope target = SexScope.fromScope(desiredTarget.getDesiredSexScope());
        if (target.equals(SexScope.SCOPE_ALL))
            return EQUAL_SEX;
        if (target.getScope() == targetUser.getDesiredSexScope())
            return EQUAL_SEX;
        else return UNEQUAL_SEX;
    }

    public double getDistanceScore() {
        return 0.5 * GPSMath.distance(desiredTarget.getGpsData().getLatitude(), targetUser.getGpsData().getLatitude(), desiredTarget.getGpsData().getLongitude(), targetUser.getGpsData().getLongitude(), 'K');
    }

    public double getTotalScore() {
        return getSexScore() + getDistanceScore() + getInterestScore();
    }

    private double getInterestScore() {

        JSONObject me = new JSONObject(desiredTarget.getUser().getInterests());
        JSONObject target = new JSONObject(targetUser.getUser().getInterests());

        JSONArray meHobby = me.getJSONArray("hobby");
        JSONArray mePlace = me.getJSONArray("place");
        JSONArray meFood = me.getJSONArray("food");

        JSONArray tHobby = target.getJSONArray("hobby");
        JSONArray tPlace = me.getJSONArray("place");
        JSONArray tFood = me.getJSONArray("food");


        //관심사가 일치할 때마다 점수를 줄 경우 관심가가 균형을 이루지않는 경우가 있으므로 표준편차를 점수에서 뺀다

        double hobbyScore = 0;
        double placeScore = 0;
        double foodScore = 0;

        for (int i = 0; i < meHobby.length(); i++) {
            for (int k = 0; k < tHobby.length(); k++) {
                if (meHobby.getString(i).equalsIgnoreCase(tHobby.getString(k)))
                    hobbyScore += EQUAL_INTEREST;
            }
        }
        for (int i = 0; i < mePlace.length(); i++) {
            for (int k = 0; k < tPlace.length(); k++) {
                if (mePlace.getString(i).equalsIgnoreCase(tPlace.getString(k)))
                    placeScore += EQUAL_INTEREST;
            }
        }
        for (int i = 0; i < meFood.length(); i++) {
            for (int k = 0; k < tFood.length(); k++) {
                if (meFood.getString(i).equalsIgnoreCase(tFood.getString(k)))
                    foodScore += EQUAL_INTEREST;
            }
        }
        double avg = (hobbyScore + placeScore + foodScore) / 3;
        double var = ((hobbyScore - avg) * (hobbyScore - avg) + (placeScore - avg) * (placeScore - avg) + (foodScore - avg) * (foodScore - avg)) / 3;
        double std = Math.sqrt(var);

        return avg - var > 0 ? avg - var : 0;
    }
}
