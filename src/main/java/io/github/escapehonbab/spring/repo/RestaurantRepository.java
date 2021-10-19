package io.github.escapehonbab.spring.repo;

import io.github.escapehonbab.jpa.objects.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findRestaurantByName(String name);

    Restaurant findRestaurantByAddress(String address);


    List<Restaurant> findRestaurantsByRate(double rate);


}
