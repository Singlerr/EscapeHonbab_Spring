package io.github.escapehonbab.spring.objects;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class FriendChatInfoList implements Serializable {
    private List<FriendChatInfo> list;

    public FriendChatInfoList(List<FriendChatInfo> list) {
        this.list = list;
    }
}