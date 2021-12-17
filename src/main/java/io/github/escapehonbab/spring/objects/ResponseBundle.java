package io.github.escapehonbab.spring.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.escapehonbab.netty.utils.ObjectSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Setter
@Getter
@Builder
public class ResponseBundle {

    private int responseCode;
    private String response;

    public void setMessage(Object o) throws JsonProcessingException {
        this.response = ObjectSerializer.getMapper().writeValueAsString(o);
    }

    public <T> T getMessage(Class<T> aClass) throws JsonProcessingException {
        return ObjectSerializer.getMapper().readValue(response, aClass);
    }
}
