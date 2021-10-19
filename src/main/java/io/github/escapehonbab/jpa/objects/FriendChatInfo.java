package io.github.escapehonbab.jpa.objects;

import io.github.escapehonbab.jpa.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Table(name = "chatroom")
@Entity
public class FriendChatInfo extends BaseModel implements Serializable {

    private byte[] img;
    private String name;
    private String userId;
    private String sex;
    private int age;
    private String time;
    private Long ownerId;

}