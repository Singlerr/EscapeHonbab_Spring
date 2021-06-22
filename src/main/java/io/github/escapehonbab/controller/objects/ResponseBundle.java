package io.github.escapehonbab.controller.objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseBundle {

    private int responseCode;
    private String response;
}
