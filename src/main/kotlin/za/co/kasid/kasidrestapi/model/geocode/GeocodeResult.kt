package za.co.kasid.kasidrestapi.model.geocode

data class GeocodeResult(
    var results: List<Result> = listOf(),
    var status: String = ""
)