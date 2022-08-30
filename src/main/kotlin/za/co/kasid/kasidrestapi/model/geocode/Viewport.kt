package za.co.kasid.kasidrestapi.model.geocode

data class Viewport(
    var northeast: Northeast = Northeast(),
    var southwest: SouthwestX = SouthwestX()
)