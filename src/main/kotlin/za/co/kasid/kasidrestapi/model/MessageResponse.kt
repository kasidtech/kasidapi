package za.co.kasid.kasidrestapi.model

data class MessageResponse(
    var messaging_product: String = "",
    var contacts: List<Contact> = listOf(),
    var messages: List<MessageX> = listOf()
)