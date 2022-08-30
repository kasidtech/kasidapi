package za.co.kasid.kasidrestapi.model.order

data class PlaceOrderRequest(
    var cart: ArrayList<Cart> = arrayListOf(),
    var coupon_discount_amount: Double = 0.0,
    var coupon_discount_title: String? = null,
    var order_amount: Double = 0.0,
    var order_type: String = "",
    var payment_method: String = "",
    var order_note: String = "",
    var coupon_code: String? = null,
    var restaurant_id: Int? = 0,
    var distance: Double? = 0.0,
    var schedule_at: String? = null,
    var discount_amount: Double = 0.0,
    var tax_amount: Double = 0.0,
    var address: String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    var contact_person_name: String = "",
    var contact_person_number: String = "",
    var address_type: String = "",
    var road: String = "",
    var house: String = "",
    var floor: String = "",
    var dm_tips: String = ""
)