package za.co.kasid.kasidrestapi.model.zone

data class ZoneListResponse(
    var zone_id: String = "",
    var zone_data: List<ZoneData> = listOf(),
    var lat: String? = "",
    var long: String? = ""
)