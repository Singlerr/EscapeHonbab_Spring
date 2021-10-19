package io.github.escapehonbab.spring.objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseBundle {

    private int responseCode;
    private String response;
}
