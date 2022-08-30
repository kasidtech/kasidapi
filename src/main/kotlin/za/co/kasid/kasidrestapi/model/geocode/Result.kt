package za.co.kasid.kasidrestapi.model.geocode

data class Result(
    var address_components: List<AddressComponent> = listOf(),
    var formatted_address: String = "",
    var geometry: Geometry = Geometry(),
    var place_id: String = "",
    var types: List<String> = listOf()
)