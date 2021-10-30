package io.github.escapehonbab.spring.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.escapehonbab.netty.utils.ObjectSerializer;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Getter
public class RequestBundle {

    private String message;

    public RequestBundle(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(Object o) throws JsonProcessingException {
        this.message = ObjectSerializer.getMapper().writeValueAsString(o);
    }

    public <T> T getMessage(Class<T> aClass) throws IOException {
        return ObjectSerializer.getMapper().readValue(message,aClass);
    }
}
