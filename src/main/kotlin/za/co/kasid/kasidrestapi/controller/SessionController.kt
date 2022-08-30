package za.co.kasid.kasidrestapi.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.service.SessionService

@RequestMapping("api/v1/session")
@RestController
class SessionController(val service: SessionService) {

    @GetMapping
    fun getAllSessions() = service.getAll()

    @GetMapping("/{id}")
    fun getSession(@PathVariable id: Long) = service.getById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveSession(@RequestBody session: WhatsAppSession): WhatsAppSession = service.create(session)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSession(@PathVariable id: Long) = service.remove(id)

    @PutMapping("/{id}")
    fun updateSession(
        @PathVariable id: Long, @RequestBody session: WhatsAppSession
    ) = service.update(id, session)

}