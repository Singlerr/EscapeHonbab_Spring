package io.github.escapehonbab.jpa.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MessageBundle {
    @Setter
    private String message;
    @Setter
    private String state;
    private String targetUserId;
    private User user;

    public MessageBundle(String message, String targetUserId, User user) {
        this.message = message;
        this.targetUserId = targetUserId;
        this.user = user;
    }

    public MessageBundle() {
    }

    public static MessageBundle createMessage(String message, String targetUserId, User user) {
        return new MessageBundle(message, targetUserId, user);
    }
}
