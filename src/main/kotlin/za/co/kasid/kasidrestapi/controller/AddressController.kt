package za.co.kasid.kasidrestapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import za.co.kasid.kasidrestapi.service.AddressService
import za.co.kasid.kasidrestapi.service.FoodService

@RequestMapping("api/v1/food")
@RestController
class AddressController(val service: AddressService) {

    /* @GetMapping
     fun getAllFood() = service.getAll()*/

    @GetMapping("number/{number}")
    fun getByContactNumber(@PathVariable("number") number: String) = service.getByContactPersonNumber(number)

    @GetMapping("user/{id}")
    fun getByUserId(@PathVariable("id") id: Int) = service.getByUserId(id)

    @GetMapping("zone/{id}")
    fun getByZoneId(@PathVariable("id") id: Int) = service.getByZoneId(id)
}