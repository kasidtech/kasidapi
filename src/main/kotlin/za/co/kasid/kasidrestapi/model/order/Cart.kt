package za.co.kasid.kasidrestapi.model.order

data class Cart(
    var food_id: Int? = 0,
    var item_campaign_id: Int? = 0,
    var price: String? = "",
    var variant: String = "",
    var variation: List<Variation> = listOf(),
    var quantity: Int = 0,
    var add_on_ids: List<Int> = listOf(),
    var add_ons: List<AddOns> = listOf(),
    var add_on_qtys: List<Int> = listOf()
)