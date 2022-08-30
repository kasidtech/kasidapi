package za.co.kasid.kasidrestapi.model.message_recieved

data class UserMessage(
    var `object`: String = "",
    var entry: List<Entry> = listOf()
)