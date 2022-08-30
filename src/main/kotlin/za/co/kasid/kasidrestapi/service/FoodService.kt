package za.co.kasid.kasidrestapi.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.Zone
import za.co.kasid.kasidrestapi.repository.FoodRepository
import za.co.kasid.kasidrestapi.repository.ZoneRepository

@Service
class FoodService(val repository: FoodRepository) {

    fun getAll(): List<FoodItem> = repository.findAll()

    fun getByRestaurantId(id: Int): List<FoodItem?> = repository.findByRestaurantid(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}