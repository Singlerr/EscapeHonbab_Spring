package io.github.escapehonbab.controller.objects

import io.github.escapehonbab.jpa.objects.Restaurant
import java.io.Serializable

class RestaurantList(var restaurants: List<Restaurant>) : Serializable