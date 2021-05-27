package io.github.escapehonbab.jpa.objects;

import io.ebean.annotation.DbArray;
import lombok.Builder;
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
 * @see io.github.escapehonbab.jpa.objects.BaseModel
 * This is a object that contains base information of a user.
 */

@Builder
@Setter
@Getter
@Table(name = "users")
@Entity
public class User extends BaseModel{
    /**
     * A user id
     */
    private String id;
    /**
     * A real name of a user, not a nick name.
     */
    private String name;

    /**
     * A nick name of a user.
     */
    private String nickName;

    /**
     * A birth day of a user.
     */
    private Date birthDay;

    /**
     * A sex of a user. It can be parsed as enum.
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
    @DbArray
    private List<String> interests;

    /**
     * A form of this is not set.
     * TODO("Set a form of phone number")
     */
    private String phoneNumber;

    /**
     * It is not saved to a database.
     * It is basically null when an instance of user is created.
     * You should set gps data manually to a gps data from rest api.
     */
    @Transient
    private GPSData gpsData;


    public Sex getSexType(){
        return Sex.valueOf(getSex());
    }
}
