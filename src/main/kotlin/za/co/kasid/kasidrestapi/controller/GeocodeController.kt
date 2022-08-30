package za.co.kasid.kasidrestapi.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import za.co.kasid.kasidrestapi.model.geocode.GeocodeResult
import java.lang.reflect.Type
import java.net.URLEncoder

@RequestMapping("api/v1/geocode")
@RestController
class GeocodeController {

    companion object {
        const val URL = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyBFK6WS60waNDHg0e4DBwbJot0vufCdp5U"
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getGeocode(@RequestParam address: String?): GeocodeResult? {
        val formattedAddress = address?.replace(" ", "%")
        val restTemplate = RestTemplate()
        val response = restTemplate.exchange("$URL&address=$formattedAddress", HttpMethod.GET, null, String::class.java)
        val type: Type = object : TypeToken<GeocodeResult>() {}.type
        return Gson().fromJson(response.body, type)
    }
}