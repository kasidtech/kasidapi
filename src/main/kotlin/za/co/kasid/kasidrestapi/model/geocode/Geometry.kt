package za.co.kasid.kasidrestapi.model.geocode

data class Geometry(
    var bounds: Bounds = Bounds(),
    var location: Location = Location(),
    var location_type: String = "",
    var viewport: Viewport = Viewport()
)