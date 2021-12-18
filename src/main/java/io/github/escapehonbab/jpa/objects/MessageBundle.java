package io.github.escapehonbab.jpa.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Getter
public class MessageBundle {
    @Setter
    private String message;
    @Setter
    private String state;
    private String targetUserId;
    private String ownerId;

    private String time;

    public MessageBundle() {
    }

    public static MessageBundle createMessage(String message, String targetUserId, String ownerId, String time, String state) {
        return new MessageBundle(message, state, targetUserId, ownerId, time);
    }
}
