package io.github.escapehonbab.jpa.objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.escapehonbab.jpa.model.BaseModel;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Singlerr
 * @see BaseModel
 * This is a object that contains base information of a user.
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME)
@Setter
@Getter
@Table(name = "users")
@Entity
public class User extends BaseModel implements Serializable {

    /**
     * A user id
     */
    private String userId;
    /**
     * A user password
     */
    private String password;
    /**
     * A real name of a user, not a nick name.
     */
    private String name;
    /**
     * A nick name of a user.
     */
    private String nickName;

    /**
     * An age of a user
     */
    private int age;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> blackList;

    /**
     * A set of friends.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> friends;
    /**
     * A birth day of a user.
     */
    private LocalDate birthDay;
    /**
     * A sex of a user. It can be parsed as enum.
     *
     * @see Sex
     */
    private String sex;
    /**
     * A image of a profile of user.
     * It is saved with type "Blob"
     */
    @Lob
    private byte[] image;
    /**
     * A set of interests that a user has.
     * All interests are transformed from Korean to English in app.
     **/

    private String interests;
    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    private String phoneNumber;
    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains result message from server when register/login operation occurred.
     */
    @Transient
    private String result;
    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains responseCode from server when register/login operation occurred.
     */
    @Transient
    private int responseCode;

    public Sex getSexType() {
        return Sex.valueOf(getSex());
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                ", blackList=" + blackList +
                ", friends=" + friends +
                ", birthDay=" + birthDay +
                ", sex='" + sex + '\'' +
                ", image=" + Arrays.toString(image) +
                ", interests='" + interests + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", result='" + result + '\'' +
                ", responseCode=" + responseCode +
                '}';
    }

    public User deepCopy() {
        return User.builder().userId(userId).password(password).name(name).nickName(nickName).age(age)
                .blackList(new HashSet<>(blackList)).friends(new HashSet<>(friends)).birthDay(birthDay)
                .sex(sex).image(image).interests(interests).phoneNumber(phoneNumber).build();
    }
}
