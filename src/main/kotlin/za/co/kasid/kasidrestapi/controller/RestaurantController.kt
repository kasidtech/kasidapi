package za.co.kasid.kasidrestapi.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.service.RestaurantService

@RequestMapping("api/v1/restaurants")
@RestController
class RestaurantController(val service: RestaurantService) {

    @GetMapping
    fun getAllRestaurants() = service.getAll()

    @GetMapping("/{id}")
    fun getRestaurant(@PathVariable id: Long) = service.getById(id)

    @GetMapping("zone/{id}")
    fun getRestaurantsByZone(@PathVariable id: Int) = service.getByZoneId(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveRestaurant(@RequestBody restaurant: Restaurant): Restaurant = service.create(restaurant)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRestaurant(@PathVariable id: Long) = service.remove(id)

    @PutMapping("/{id}")
    fun updateRestaurant(
        @PathVariable id: Long, @RequestBody restaurant: Restaurant
    ) = service.update(id, restaurant)

}