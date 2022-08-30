package za.co.kasid.kasidrestapi.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContexts
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import za.co.kasid.kasidrestapi.model.MessageResponse
import za.co.kasid.kasidrestapi.model.Zone
import za.co.kasid.kasidrestapi.model.order.PlaceOrderRequest
import za.co.kasid.kasidrestapi.model.order.success.PlaceOrderResponse
import za.co.kasid.kasidrestapi.model.zone.ZoneListResponse
import java.lang.reflect.Type
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext


@RequestMapping("api/v1/order")
@RestController
class OrderController {

    companion object {
        const val PAYMENT_TEMPLATE = "https://app.kasid.co.za/payment-mobile?customer_id=1&order_id=100849"
        const val PLACE_ORDER_URI = "http://app.kasid.co.za/api/v1/customer/order/place"
        const val GET_ZONES_URI = "http://app.kasid.co.za/api/v1/config/get-zone-id"
        const val TOKEN =
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNGRhNjViMDA2YjZkNjNmMzkwOGZlODhhMjYyYzNkZDMyZGY0ZDFiMmFmYmU3ZmMyMzliNTc2ZWYxMTVmYmQzNjM5NjAzNGYzNTUyMDNlN2QiLCJpYXQiOjE2NjE2ODE0NDcuOTI2MjIzLCJuYmYiOjE2NjE2ODE0NDcuOTI2MjI1LCJleHAiOjE2OTMyMTc0NDcuOTI0NjIyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.aHsfvyXiQq-ZBd6SnEdMMgBwJIIMB2WoOtjm_KQjT1ifCCV3es2h_5iOBTYxV51u9hdALerhhZC0jzVElshyTxpXU_wqtirnml6tOtBuUcj6cUhNDaDZJhuZGcnowlK2VfMFCMa6P6WjrIIwGk5a-eEz5vgYSXvWi8MpU1UCCPI2Ew-WiR9LquWbdAd9r8fnPsaLjJL2qpGhFRUQB1aEV6Np29vjV2icYjCwiKz4UXtrMRBG72YiEQNGLBcmdB-VYae_wydqyfqMX48TLWY5oXkHGgdMyRIn1GipDx5SK9ExGXdQd2pUaLTkyN8yH_BmlEf9fghd044HB_4ODKOtPVLQ0Bs0xuoihYBJ6XUrVH9bvdvBaGWQ8heApTFJEqNkQ553DuTGs0roS0pOa_NaZ15K5HOIDAhIrgA4EIFSZJE1emv_zvVkcHVBv-kY_N_GY9_ThVWkpU0_jVg-ZhYGtG8VmSos8wqA-Ysp_BwpE88kdAVNzsd7kdXuv1tQWISOJlp_eJ4Xhms37BCN8xHInxXo_9YsknbzgdNigBcQ4DiL6Yb3E2ZhJCtXa8_O5sF7ji4xOV0KG7_iPEw-0u1i9xVmOknhkiCkG46pOcYKU5F1GurjMZa2kWghv-YgbfFZg_yGtlcpTGinmlWHFS8Txuo2ATQ3D6w-ucv5guUZTEM"
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun placeOrder(@RequestBody placeOrderRequest: PlaceOrderRequest): PlaceOrderResponse? {
        val sslContext: SSLContext = SSLContexts.custom().loadTrustMaterial(null, TrustStrategy { _: Array<X509Certificate?>?, _: String? -> true }).build()
        val closeableHttpClient: CloseableHttpClient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory(sslContext)).build()
        val requestFactory = HttpComponentsClientHttpRequestFactory().apply { httpClient = closeableHttpClient }
        val restTemplate = RestTemplate(requestFactory)
        val entity = HttpEntity(Gson().toJson(placeOrderRequest), createKasidHeaders())
        val response = restTemplate.exchange(PLACE_ORDER_URI, HttpMethod.POST, entity, String::class.java)
        val type: Type = object : TypeToken<PlaceOrderResponse>() {}.type
        return Gson().fromJson(response.body, type)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getZones(@RequestParam address: String): ZoneListResponse {
        val geocode = GeocodeController().getGeocode(address)
        if (geocode != null) {
            val link = GET_ZONES_URI + "?lat=${geocode.results[0].geometry.location.lat}&lng=${geocode.results.get(0).geometry.location.lng}"
            val sslContext: SSLContext = SSLContexts.custom().loadTrustMaterial(null, TrustStrategy { _: Array<X509Certificate?>?, _: String? -> true }).build()
            val closeableHttpClient: CloseableHttpClient = HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory(sslContext)).build()
            val requestFactory = HttpComponentsClientHttpRequestFactory().apply { httpClient = closeableHttpClient }
            val restTemplate = RestTemplate(requestFactory)
            val entity = HttpEntity(null, createKasidHeaders())
            val response: ResponseEntity<String>
            return try {
                response = restTemplate.exchange(link, HttpMethod.GET, entity, String::class.java)
                val type: Type = object : TypeToken<ZoneListResponse>() {}.type
                val zoneList: ZoneListResponse = Gson().fromJson(response.body, type)
                zoneList.lat = geocode.results[0].geometry.location.lat.toString()
                zoneList.long = geocode.results[0].geometry.location.lng.toString()
                return zoneList
            } catch (e: HttpClientErrorException) {
                ZoneListResponse()
            }
        }
        return ZoneListResponse()
    }

    fun createKasidHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("content-Type", "application/json; charset=UTF-8")
        headers.set("zoneid", "[1]")
        headers.set("x-localization", "en")
        headers.set("Authorization", TOKEN)
        return headers
    }

}