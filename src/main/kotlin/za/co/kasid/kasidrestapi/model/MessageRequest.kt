package za.co.kasid.kasidrestapi.model

data class MessageRequest(
    var messaging_product: String = "whatsapp",
    var recipient_type: String = "individual",
    var to: String = "",
    var type: String = "text",
    var text: Text = Text()
)