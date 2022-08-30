package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.model.address.CustomerAddress

@Repository
interface AddressRepository : JpaRepository<CustomerAddress, Long> {
    fun findById(@Param(value = "id") id: Long?): List<CustomerAddress?>?
    fun findByUserid(@Param(value = "user_id") id: Int?): List<CustomerAddress?>?
    fun findByZoneid(@Param(value = "zone_id") id: Int?): List<CustomerAddress?>?
    fun findByContactpersonnumber(@Param(value = "contact_person_number") id: String?): List<CustomerAddress?>?
}