package io.github.escapehonbab.jpa.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Getter
public class MessageBundle {
    @Setter
    private String message;
    @Setter
    private String state;
    private String targetUserId;
    private String ownerId;

    public MessageBundle(String message, String targetUserId, String ownerId) {
        this.message = message;
        this.targetUserId = targetUserId;
        this.ownerId = ownerId;
    }

    public MessageBundle() {
    }

    public static MessageBundle createMessage(String message, String targetUserId, String ownerId) {
        return new MessageBundle(message, targetUserId, ownerId);
    }
}
