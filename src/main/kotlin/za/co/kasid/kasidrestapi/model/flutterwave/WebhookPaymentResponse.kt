package za.co.kasid.kasidrestapi.model.flutterwave

data class WebhookPaymentResponse(
    var event: String = "",
    var `data`: Data = Data(),
)