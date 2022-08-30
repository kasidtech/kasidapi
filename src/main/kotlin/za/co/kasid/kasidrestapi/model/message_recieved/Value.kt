package za.co.kasid.kasidrestapi.model.message_recieved

data class Value(
    var messaging_product: String = "",
    var metadata: Metadata = Metadata(),
    var contacts: List<Contact> = listOf(),
    var messages: List<Message> = listOf()
)