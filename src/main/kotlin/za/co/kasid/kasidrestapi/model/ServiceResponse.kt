package za.co.kasid.kasidrestapi.model

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity


class ServiceResponse<T> : ResponseEntity<T> {
    constructor(data: T) : super(data, HttpStatus.OK) {}
    constructor(data: T, status: HttpStatus) : super(data, status) {}

    companion object {
        private val objectMapper = ObjectMapper()
        val defaultServiceOkResponse: ServiceResponse<Any>
            get() {
                val objectNode: ObjectNode = objectMapper.createObjectNode()
                objectNode.put("message", "Success")
                return ServiceResponse(objectNode, HttpStatus.OK)
            }

        fun getDefaultServiceFailedResponse(message: String?): ServiceResponse<JsonNode> {
            val objectNode: ObjectNode = objectMapper.createObjectNode()
            objectNode.put("message", message)
            return ServiceResponse(objectNode, HttpStatus.BAD_REQUEST)
        }

        /**
         * OK response with some message
         *
         * @param message
         * @return
         */
        fun getDefaultServiceOkResponse(message: String?): ServiceResponse<JsonNode> {
            val objectNode: ObjectNode = objectMapper.createObjectNode()
            objectNode.put("message", message)
            return ServiceResponse(objectNode, HttpStatus.OK)
        }
    }
}