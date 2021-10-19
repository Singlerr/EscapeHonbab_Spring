package io.github.escapehonbab.jpa.objects;

import io.github.escapehonbab.jpa.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Table(name = "restaurants")
@Entity
public class Restaurant extends BaseModel implements Serializable {

    private String name;
    private double rate;
    private String category;
    private String address;
    private byte[] image;
}