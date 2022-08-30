package za.co.kasid.kasidrestapi.util

import za.co.kasid.kasidrestapi.model.MessageRequest
import za.co.kasid.kasidrestapi.model.Text
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import java.util.regex.Pattern

object MessageUtil {

    fun isNumeric(message: String): Boolean {
        return message.chars().allMatch { x -> Character.isDigit(x) }
    }

    fun messageIsNotNumericError(): String {
        return "Please enter a number only"
    }

    fun createWhatsAppMessage(message: String, userMessage: UserMessage): MessageRequest {
        val messageRequest = MessageRequest()
        messageRequest.apply {
            to = userMessage.entry.first().changes.first().value.messages.first().from
            text = Text(body = message)
        }
        return messageRequest
    }

    fun getUserResponse(userMessage: UserMessage): String {
        return userMessage.entry.first().changes.first().value.messages.first().text.body.lowercase()
    }

    fun getUserNumber(userMessage: UserMessage): String {
        return userMessage.entry.first().changes.first().value.messages.first().from
    }

    fun createWhatsAppMessageWithoutResponse(message: String, number: String?): MessageRequest {
        val messageRequest = MessageRequest()
        if (!number.isNullOrEmpty()) {
            messageRequest.apply {
                to = number
                text = Text(body = message)
            }
        }
        return messageRequest
    }

}