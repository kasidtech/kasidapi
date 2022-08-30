package za.co.kasid.kasidrestapi.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.repository.RestaurantRepository

@Service
class RestaurantService(val repository: RestaurantRepository) {

    fun getAll(): List<Restaurant> = repository.findAll()

    fun getById(id: Long): Restaurant = repository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByZoneId(id: Int): List<Restaurant?> = repository.findByZoneid(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun create(restaurant: Restaurant): Restaurant = repository.save(restaurant)

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, restaurant: Restaurant): Restaurant {
        return if (repository.existsById(id)) {
            restaurant.id = id
            repository.save(restaurant)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

}