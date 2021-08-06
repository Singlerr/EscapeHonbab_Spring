package io.github.escapehonbab.jpa.objects;

import io.github.escapehonbab.jpa.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Date;
import java.util.List;

/**
 * @author Singlerr
 * @see BaseModel
 * This is a object that contains base information of a user.
 */


@Setter
@Getter
@Table(name = "users")
@Entity
public class User extends BaseModel {
    /**
     * A user id
     */
    public String userId;
    /**
     * A user password
     */
    public String password;
    /**
     * A real name of a user, not a nick name.
     */
    public String name;
    /**
     * A nick name of a user.
     */
    public String nickName;
    /**
     * A set of friends.
     */

    public List<String> friends;
    /**
     * A birth day of a user.
     */
    public Date birthDay;
    /**
     * A sex of a user. It can be parsed as enum.
     *
     * @see Sex
     */
    public String sex;
    /**
     * A image of a profile of user.
     * It is saved with type "Blob"
     */
    @Lob
    public byte[] image;
    /**
     * A set of interests that a user has.
     * All interests are transformed from Korean to English in app.
     **/

    public String interests;
    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    public String phoneNumber;
    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * You should set gps data manually to a gps data from rest api.
     */
    @Transient
    public GPSData gpsData;
    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains result message from server when register/login operation occurred.
     */
    @Transient
    public String result;
    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * It contains responseCode from server when register/login operation occurred.
     */
    @Transient
    public int responseCode;
    public User() {
    }

    public Sex getSexType() {
        return Sex.valueOf(getSex());
    }
}
