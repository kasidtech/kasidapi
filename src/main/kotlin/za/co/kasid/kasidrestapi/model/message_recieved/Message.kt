package za.co.kasid.kasidrestapi.model.message_recieved

data class Message(
    var from: String = "",
    var id: String = "",
    var timestamp: String = "",
    var text: Text = Text(),
    var type: String = ""
)