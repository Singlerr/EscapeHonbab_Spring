package io.github.escapehonbab.jpa.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Setter
@Getter
public class DesiredTarget implements Serializable {
    private User user;
    private int desiredAgeScope = 0;
    private int desiredSexScope = 0;
    private GPSData gpsData;

    public DesiredTarget() {
    }
}
