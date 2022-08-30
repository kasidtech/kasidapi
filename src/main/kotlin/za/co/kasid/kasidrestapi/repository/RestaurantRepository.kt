package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.Restaurant

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findById(@Param(value = "id") id: Long?): List<Restaurant?>?
    fun findByZoneid(@Param(value = "zone_id") id: Int?): List<Restaurant?>?
}