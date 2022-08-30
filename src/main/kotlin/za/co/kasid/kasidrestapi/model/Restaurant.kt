package za.co.kasid.kasidrestapi.model

import javax.persistence.*

@Entity
@Table(name = "restaurants")
@Cacheable(false)
data class Restaurant(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var name: String = "",
    var phone: String = "",

    @Column(name = "zone_id", nullable = false, updatable = false)
    var zoneid: Int = 0
)