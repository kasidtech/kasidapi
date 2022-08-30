package za.co.kasid.kasidrestapi.model.geocode

data class AddressComponent(
    var long_name: String = "",
    var short_name: String = "",
    var types: List<String> = listOf()
)