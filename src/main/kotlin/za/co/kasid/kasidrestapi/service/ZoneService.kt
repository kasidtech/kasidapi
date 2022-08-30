package za.co.kasid.kasidrestapi.service

import org.springframework.stereotype.Service
import za.co.kasid.kasidrestapi.model.Zone
import za.co.kasid.kasidrestapi.repository.ZoneRepository

@Service
class ZoneService(val repository: ZoneRepository) {

    fun getAll(): List<Zone> = repository.findAll()

}