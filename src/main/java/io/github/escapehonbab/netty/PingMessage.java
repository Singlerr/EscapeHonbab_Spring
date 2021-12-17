package io.github.escapehonbab.netty;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
public class PingMessage implements Serializable {
    public PingMessage() {
    }
}