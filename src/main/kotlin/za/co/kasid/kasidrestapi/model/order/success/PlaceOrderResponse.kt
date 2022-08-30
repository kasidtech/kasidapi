package za.co.kasid.kasidrestapi.model.order.success

data class PlaceOrderResponse(
    var message: String = "",
    var order_id: Int = 0,
    var total_ammount: Double = 0.0
)