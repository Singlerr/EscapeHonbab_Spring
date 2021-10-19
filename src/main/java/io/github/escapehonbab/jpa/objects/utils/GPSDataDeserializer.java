package io.github.escapehonbab.jpa.objects.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.escapehonbab.jpa.objects.GPSData;

import java.io.IOException;

public class GPSDataDeserializer extends JsonDeserializer<GPSData> {

    @Override
    public GPSData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return new ObjectMapper().readValue(jsonParser.getValueAsString(), GPSData.class);
    }
}
