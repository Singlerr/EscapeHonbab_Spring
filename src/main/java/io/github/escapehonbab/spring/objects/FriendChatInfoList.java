package io.github.escapehonbab.spring.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Getter
@Setter
public class FriendChatInfoList implements Serializable {
    private List<FriendChatInfo> list;

    public FriendChatInfoList(List<FriendChatInfo> list) {
        this.list = list;
    }
}