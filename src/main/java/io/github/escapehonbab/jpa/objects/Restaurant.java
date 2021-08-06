package io.github.escapehonbab.jpa.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Setter
@Getter
@Table(name = "users")
@Entity
public class Restaurant {
}
