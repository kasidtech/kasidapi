package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.SelectedFood

@Repository
interface CartRepository: JpaRepository<SelectedFood, Long> {
    fun findById(@Param(value = "id") id: Long?): List<SelectedFood?>?
    fun findByReference(@Param(value = "reference") reference: String?): List<SelectedFood?>?
}