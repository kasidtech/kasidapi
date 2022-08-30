package za.co.kasid.kasidrestapi.model.address

import javax.persistence.*

@Entity
@Table(name = "customer_addresses")
@Cacheable(false)
data class CustomerAddress(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long,
    var address: String = "",

    @Column(name = "latitude", nullable = false, updatable = false)
    var latitude: String = "",

    @Column(name = "longitude", nullable = false, updatable = false)
    var longitude: String = "",

    @Column(name = "contact_person_number", nullable = false, updatable = false)
    var contactpersonnumber: String = "",

    @Column(name = "zone_id", nullable = false, updatable = false)
    var zoneid: Int = 0,

    @Column(name = "user_id", nullable = false, updatable = false)
    var userid: Int = 0
)