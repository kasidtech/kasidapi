package za.co.kasid.kasidrestapi.util

import org.springframework.beans.factory.annotation.Autowired
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.StepEnumV2.*
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.*
import za.co.kasid.kasidrestapi.util.MessageUtil.getUserNumber
import za.co.kasid.kasidrestapi.util.TimeStampUtil.isTimeStampExpired
import za.co.kasid.kasidrestapi.util.step.*
import javax.persistence.EntityManager


object StepHandler {

    @Autowired
    private val entityManager: EntityManager? = null

    fun handleStep(userMessage: UserMessage, sessionService: SessionService, restaurantService: RestaurantService, foodService: FoodService, addressService: AddressService, cartService: CartService): Any {
        entityManager?.clear()
        val session = sessionService.getByNumber(getUserNumber(userMessage)).lastOrNull()
        if (session != null) {
            if (isTimeStampExpired(session)) {
                SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = SESSION_EXPIRED })
                MessageController().sendMessage(MessageUtil.createWhatsAppMessage(StepUtil.createSessionExpiredMessage(), userMessage))
                MessageController().sendMessage(MessageUtil.createWhatsAppMessage(StepUtil.createFirstMessage(), userMessage))
                SessionController(sessionService).saveSession(createNewSession(userMessage))
                return "End"
            }
            when (session.sessionStep) {
                NEW_SESSION -> MainMenu.createMainMenuMessage(userMessage)
                MAIN_MENU_PAGE -> MainMenu.mainMenu(userMessage, sessionService, addressService)
                SELECT_ADDRESS_PAGE -> AddressStep.manageSelectAddress(userMessage, sessionService, addressService, restaurantService)
                ADD_ADDRESS_PAGE -> AddressStep.manageAddAddress(userMessage, sessionService, restaurantService)
                SELECT_RESTAURANT_PAGE -> SelectRestaurant.manageSelectedRestaurant(userMessage, sessionService, restaurantService, foodService)
                FOOD_MENU_PAGE -> FoodMenu.manageSelectedFood(userMessage, sessionService, foodService, cartService)
                CHECKOUT_PAGE -> Checkout.manageSelectedCheckout(userMessage, sessionService, cartService)
                //AWAITING_CARD_PAYMENT_PAGE ->  AwaitingCardPayment.paymentNotComplete(sessionService, userMessage)
                //PAYMENT_FAILED -> AwaitingCardPayment.paymentFailed(sessionService, userMessage, cartService)
                //When creating an order make sure to assign order id to the session
                ORDER_CONFIRMED -> OrderConfirmed.manageOrderSelection(sessionService, userMessage)
                SESSION_EXPIRED -> SessionExpired.handleSessionExpired(userMessage, sessionService)
                VIEW_PREVIOUS_ORDERS_PAGE -> {
                    //TODO
                }
                ORDER_DETAILS_PAGE -> {
                    //TODO
                }
                AGENT_CHAT_PAGE -> {
                    //TODO
                }
            }
        } else {
            SessionController(sessionService).saveSession(createNewSession(userMessage))
            MainMenu.createMainMenuMessage(userMessage)
        }
        return "End"
    }

    fun createNewSession(body: UserMessage): WhatsAppSession {
        return WhatsAppSession().apply {
            sessionStep = MAIN_MENU_PAGE
            userName = body.entry.first().changes.first().value.contacts.first().profile.name
            userNumber = body.entry.first().changes.first().value.messages.first().from
            reference = userNumber + (System.currentTimeMillis() / 1000L).toString()
            sessionLeft = (System.currentTimeMillis() / 1000L).toString()
        }
    }

}