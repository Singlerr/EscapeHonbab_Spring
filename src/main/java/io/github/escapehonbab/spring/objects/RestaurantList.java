package io.github.escapehonbab.spring.objects;

import io.github.escapehonbab.jpa.objects.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class RestaurantList implements Serializable {
    private List<Restaurant> list;

    public RestaurantList(List<Restaurant> list) {
        this.list = list;
    }
}