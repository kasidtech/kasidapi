package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.Zone

@Repository
interface FoodRepository : JpaRepository<FoodItem, Long> {
    fun findById(@Param(value = "id") id: Long?): List<FoodItem?>?
    fun findByRestaurantid(@Param(value = "restaurant_id") id: Int?): List<FoodItem?>?
}