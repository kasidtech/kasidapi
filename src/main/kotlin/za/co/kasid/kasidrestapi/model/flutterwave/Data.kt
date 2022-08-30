package za.co.kasid.kasidrestapi.model.flutterwave

data class Data(
    var id: Int = 0,
    var tx_ref: String? = "",
    var flw_ref: String = "",
    var device_fingerprint: String = "",
    var amount: Int = 0,
    var currency: String = "",
    var charged_amount: Int = 0,
    var app_fee: Double = 0.0,
    var merchant_fee: Int = 0,
    var processor_response: String = "",
    var auth_model: String = "",
    var ip: String = "",
    var narration: String = "",
    var status: String = "",
    var payment_type: String = "",
    var created_at: String = "",
    var account_id: Int = 0,
    var customer: Customer = Customer(),
    var card: Card = Card()
)