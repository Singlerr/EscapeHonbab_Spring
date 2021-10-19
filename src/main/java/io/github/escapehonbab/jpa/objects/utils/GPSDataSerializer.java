package io.github.escapehonbab.jpa.objects.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.escapehonbab.jpa.objects.GPSData;

import java.io.IOException;

public class GPSDataSerializer extends JsonSerializer<GPSData> {
    @Override
    public void serialize(GPSData gpsData, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(gpsData);
    }
}
