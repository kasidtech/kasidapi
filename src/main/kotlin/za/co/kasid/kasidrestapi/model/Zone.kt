package za.co.kasid.kasidrestapi.model

import javax.persistence.*

@Entity
@Table(name = "zones")
@Cacheable(false)
data class Zone(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var name: String = "",
    var status: Int = 0
)