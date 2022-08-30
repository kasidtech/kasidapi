package za.co.kasid.kasidrestapi.model

data class LoginRequest(
    var phone: String = "",
    var password: String = ""
)
