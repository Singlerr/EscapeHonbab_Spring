package io.github.escapehonbab.spring.service;

import io.github.escapehonbab.jpa.objects.Restaurant;
import io.github.escapehonbab.spring.repo.RestaurantRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Builder
@Service
public class RestaurantService {

    private RestaurantRepository repository;


    public List<Restaurant> findAll() {
        return repository.findAll();
    }

    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id);
    }

    public List<Restaurant> findAllByRate(double rate) {
        return repository.findRestaurantsByRate(rate);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public void updateById(Long id, Restaurant restaurant) {
        Optional<Restaurant> r = repository.findById(id);
        if (r.isPresent()) {
            r.get().setId(id);
            r.get().setAddress(restaurant.getAddress());
            r.get().setCategory(restaurant.getCategory());
            r.get().setImage(restaurant.getImage());
            r.get().setName(restaurant.getName());
            r.get().setRate(restaurant.getRate());
            repository.save(restaurant);
        }
    }

}
