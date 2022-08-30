package za.co.kasid.kasidrestapi.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import za.co.kasid.kasidrestapi.service.ZoneService

@RequestMapping("api/v1/zones")
@RestController
class ZoneController(val service: ZoneService) {

    @GetMapping
    fun getAllZones() = service.getAll().filter { predicate -> predicate.status == 1 }

}