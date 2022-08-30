package za.co.kasid.kasidrestapi.model.flutterwave

data class Customer(
    var id: Int = 0,
    var name: String = "",
    var phone_number: Any = Any(),
    var email: String = "",
    var created_at: String = ""
)