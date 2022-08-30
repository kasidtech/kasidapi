package za.co.kasid.kasidrestapi.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import za.co.kasid.kasidrestapi.model.MessageRequest
import za.co.kasid.kasidrestapi.model.MessageResponse
import java.lang.reflect.Type


@RequestMapping("api/v1/messages")
@RestController
class MessageController {

    companion object {
        const val ENDPOINT = "https://graph.facebook.com/v14.0/101711582676201/messages"
        const val TOKEN = "EAAIxsDBqxk4BACdNSODlh75bhidzEf598pRifNZC3dzyzcd1vZCUd8iWYD4NooBqKoRIZAmHVY5DcfIa3NmrqQTEwHHIBJZCZCyZBjiZC4E3xu4EWjh5TenAYpyeeZAnwriNOLUNVtJLck7XlxPz8V3fSKdakRoATqFhFLZBHtiCL3GJie1sO7X7f62XhgBxaZANYXVKQvpfFZANUzun7nBtq0b2ZBodmpeuJ1kZD"
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun sendMessage(messageRequest: MessageRequest): MessageResponse? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", "Bearer $TOKEN")
        val restTemplate = RestTemplate()
        val entity = HttpEntity(Gson().toJson(messageRequest), headers)
        val response = restTemplate.exchange(ENDPOINT, HttpMethod.POST, entity, String::class.java)
        val type: Type = object : TypeToken<MessageResponse>() {}.type
        return Gson().fromJson(response.body, type)
    }

}