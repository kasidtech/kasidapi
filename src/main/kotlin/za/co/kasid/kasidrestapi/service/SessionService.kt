package za.co.kasid.kasidrestapi.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.repository.SessionRepository

@Service
class SessionService(val repository: SessionRepository) {

    fun getAll(): List<WhatsAppSession> = repository.findAll()

    fun getById(id: Long): WhatsAppSession = repository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByNumber(number: String): List<WhatsAppSession?> = repository.findByUserNumber(number) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByReference(reference: String?): WhatsAppSession = repository.findByReference(reference) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun create(session: WhatsAppSession): WhatsAppSession = repository.save(session)

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, session: WhatsAppSession): WhatsAppSession {
        return if (repository.existsById(id)) {
            session.id = id
            repository.save(session)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}