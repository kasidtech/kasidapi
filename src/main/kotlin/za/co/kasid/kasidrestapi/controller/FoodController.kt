package za.co.kasid.kasidrestapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import za.co.kasid.kasidrestapi.service.FoodService
import za.co.kasid.kasidrestapi.service.ZoneService

@RequestMapping("api/v1/food")
@RestController
class FoodController(val service: FoodService) {

   /* @GetMapping
    fun getAllFood() = service.getAll()*/

    @GetMapping("/{id}")
    fun getByRestaurantId(@PathVariable("id") id: Int) = service.getByRestaurantId(id)
}