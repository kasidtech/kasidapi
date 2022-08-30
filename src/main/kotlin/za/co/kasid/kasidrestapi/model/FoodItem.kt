package za.co.kasid.kasidrestapi.model

import javax.persistence.*

@Entity
@Table(name = "food")
@Cacheable(false)
data class FoodItem(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var name: String = "",
    var price: Double = 0.0,

    @Column(name = "restaurant_id", nullable = false, updatable = false)
    var restaurantid : Int = 0,
)
