package za.co.kasid.kasidrestapi.model.flutterwave

data class Card(
    var first_6digits: String = "",
    var last_4digits: String = "",
    var issuer: String = "",
    var country: String = "",
    var type: String = "",
    var expiry: String = ""
)