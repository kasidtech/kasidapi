package za.co.kasid.kasidrestapi.model.message_recieved

data class Change(
    var value: Value = Value(),
    var `field`: String = ""
)