package za.co.kasid.kasidrestapi.model

import javax.persistence.*

@Entity
@Table(name = "selected_food")
@Cacheable(false)
data class SelectedFood (

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
    var sessionId: String = "",

    var itemId: Int? = 0,
    var itemName: String? = "",
    var itemPrice: Double? = 0.0,

    @Column(name = "reference", nullable = false, updatable = false)
    var reference: String = ""

)