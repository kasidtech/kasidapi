package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.Zone

@Repository
interface ZoneRepository : JpaRepository<Zone, Long>