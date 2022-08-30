package za.co.kasid.kasidrestapi.model.geocode

data class Bounds(
    var northeast: Northeast = Northeast(),
    var southwest: Southwest = Southwest()
)