package za.co.kasid.kasidrestapi.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import za.co.kasid.kasidrestapi.model.SelectedFood
import za.co.kasid.kasidrestapi.repository.CartRepository

@Service
class CartService(val repository: CartRepository) {

    fun getAll(): List<SelectedFood> = repository.findAll()

    fun getById(id: Long): SelectedFood = repository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun getByReference(reference: String): List<SelectedFood?> = repository.findByReference(reference) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    fun create(selectedFood: SelectedFood): SelectedFood = repository.save(selectedFood)

    fun remove(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun update(id: Long, selectedFood: SelectedFood): SelectedFood {
        return if (repository.existsById(id)) {
            selectedFood.id = id
            repository.save(selectedFood)
        } else throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}