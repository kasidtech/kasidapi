package za.co.kasid.kasidrestapi.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.Zone
import za.co.kasid.kasidrestapi.model.address.CustomerAddress
import za.co.kasid.kasidrestapi.repository.AddressRepository
import za.co.kasid.kasidrestapi.repository.FoodRepository
import za.co.kasid.kasidrestapi.repository.ZoneRepository

@Service
class AddressService(val repository: AddressRepository) {

    fun getByZoneId(id: Int): List<CustomerAddress?> = repository.findByZoneid(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByUserId(id: Int): List<CustomerAddress?> = repository.findByUserid(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByContactPersonNumber(number: String): List<CustomerAddress?> = repository.findByContactpersonnumber(number) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}