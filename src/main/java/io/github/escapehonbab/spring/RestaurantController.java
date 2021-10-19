package io.github.escapehonbab.spring;

import io.github.escapehonbab.spring.objects.RestaurantList;
import io.github.escapehonbab.spring.service.RestaurantService;
import lombok.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@EnableAsync
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private RestaurantService service;

    @Async
    @GetMapping(value = "/get")
    public RestaurantList getRestaurantList() {
        return new RestaurantList(service.findAll());
    }
}
