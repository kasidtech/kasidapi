package za.co.kasid.kasidrestapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import za.co.kasid.kasidrestapi.model.WhatsAppSession

@Repository
interface SessionRepository : JpaRepository<WhatsAppSession, Long> {
    fun findById(@Param(value = "id") id: Long?): List<WhatsAppSession?>?
    fun findByUserNumber(@Param(value = "user_number") number: String?): List<WhatsAppSession?>?
    fun findByReference(@Param(value = "reference") reference: String?): WhatsAppSession?
}