package za.co.kasid.kasidrestapi.controller

import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import za.co.kasid.kasidrestapi.model.ServiceResponse
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.*
import za.co.kasid.kasidrestapi.util.StepHandler


@RequestMapping("api/v1/webhooks")
@RestController
class WebhookController(val sessionService: SessionService, val restaurantService: RestaurantService, val foodService: FoodService, val addressService: AddressService, val cartService: CartService) {

    @PostMapping(value = ["/whatsapp"])
    @ResponseStatus(HttpStatus.OK)
    fun messageReceived(@RequestBody body: UserMessage): ResponseEntity<HttpStatus> {
        StepHandler.handleStep(body, sessionService, restaurantService, foodService, addressService, cartService)
        println(body)
        return ResponseEntity.ok(HttpStatus.OK)
    }

    /*@GetMapping(value = ["/whatsapp"])
    @ResponseStatus(HttpStatus.OK)
    fun verifyWebhook(any: Any): ResponseEntity<Any> {
        println(Gson().toJson(any))
        return ResponseEntity.ok("WEBHOOK_VERIFIED")
    }*/

    @GetMapping(value = ["/whatsapp"])
    @ResponseStatus(HttpStatus.OK)
    fun get(@RequestParam(name = "hub.mode") hub_mode: String?, @RequestParam(name = "hub.challenge") challenge: String, @RequestParam(name = "hub.verify_token") token: String?): ServiceResponse<String> {
        return ServiceResponse(challenge, HttpStatus.OK);
    }
}