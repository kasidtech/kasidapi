package za.co.kasid.kasidrestapi.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import za.co.kasid.kasidrestapi.model.SelectedFood
import za.co.kasid.kasidrestapi.service.CartService

@RequestMapping("api/v1/cart")
@RestController
class CartController(val service: CartService) {

    @GetMapping
    fun getAllSelectedFoods() = service.getAll()

    @GetMapping("/{id}")
    fun getSelectedFood(@PathVariable id: Long) = service.getById(id)

    @GetMapping("/{reference}")
    fun getSelectedFoodByReference(@PathVariable reference: String) = service.getByReference(reference)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveSelectedFood(@RequestBody selectedFood: SelectedFood): SelectedFood = service.create(selectedFood)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSelectedFood(@PathVariable id: Long) = service.remove(id)

    @PutMapping("/{id}")
    fun updateSelectedFood(
        @PathVariable id: Long, @RequestBody selectedFood: SelectedFood
    ) = service.update(id, selectedFood)

}