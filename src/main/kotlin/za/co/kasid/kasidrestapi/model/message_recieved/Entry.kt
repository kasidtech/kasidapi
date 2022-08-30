package za.co.kasid.kasidrestapi.model.message_recieved

data class Entry(
    var id: String = "",
    var changes: List<Change> = listOf()
)