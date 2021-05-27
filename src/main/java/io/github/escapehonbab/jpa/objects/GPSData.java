package io.github.escapehonbab.jpa.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
public class GPSData implements Serializable {

    /**
     * A latitude of gps data
     */
    private double latitude;

    /**
     * A longitude of gps data
     */
    private double longitude;
    /**
     * The id of the owner of this gps data.
     */
    private String id;

    @Override
    public String toString() {
        return "GPSData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", id='" + id + '\'' +
                '}';
    }
}
