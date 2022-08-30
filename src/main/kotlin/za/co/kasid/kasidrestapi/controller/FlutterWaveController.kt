package za.co.kasid.kasidrestapi.controller

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import za.co.kasid.kasidrestapi.model.flutterwave.WebhookPaymentResponse
import za.co.kasid.kasidrestapi.service.FoodService
import za.co.kasid.kasidrestapi.service.RestaurantService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.service.ZoneService
import za.co.kasid.kasidrestapi.util.step.AwaitingCardPayment
import java.lang.reflect.Type

@RequestMapping("api/v1/flutterwave")
@RestController
class FlutterWaveController(val sessionService: SessionService) {

    @PostMapping(value = ["/paymentnotification"])
    fun paymentReceived(@RequestBody body: String): ResponseEntity<Any?> {
        /*val type: Type = object : TypeToken<WebhookPaymentResponse>() {}.type
        val paymentResponse: WebhookPaymentResponse = Gson().fromJson(body, type)
        AwaitingCardPayment.confirmOrder(paymentResponse, sessionService)*/
        return ResponseEntity.ok("")
    }

}