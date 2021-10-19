package io.github.escapehonbab.jpa.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private long version;

}
