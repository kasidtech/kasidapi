package za.co.kasid.kasidrestapi.model

import za.co.kasid.kasidrestapi.enums.SessionStepEnum
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "whatsapp_sessions")
@Cacheable(false)
data class WhatsAppSession(

    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
    var sessionId: String = "",

    var sessionStep: StepEnumV2 = StepEnumV2.MAIN_MENU_PAGE,
    var sessionTimeStamp: Date = Date(),
    var sessionUnixTimeStamp: String =  (System.currentTimeMillis() / 1000L).toString(),
    var sessionLeft: String = "",

    var userName: String = "",
    var userNumber: String = "",

    var zoneId: Int? = 0,
    var zoneName: String? = "",

    var restaurantId: Int? = 0,
    var restaurantName: String? = "",

    var itemId: Int? = 0,
    var itemName: String? = "",
    var itemPrice: Double? = 0.0,

    var address: String? = "",
    var lat: String? = "",
    var long: String? = "",
    var distance: Double? = 0.0,

    var emailAddress: String = "",

    var paymentMethod: String = "",
    var paymentLink: String = "",
    var paymentStatus: String = "",

    var orderId: Int? = 0,
    var userId: Int? = 0,

    var reference: String = ""
)
