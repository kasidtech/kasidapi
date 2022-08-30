package za.co.kasid.kasidrestapi.model.zone

data class ZoneData(
    var id: Int = 0,
    var status: String = "",
    var minimum_shipping_charge: Any = Any(),
    var per_km_shipping_charge: Any = Any()
)