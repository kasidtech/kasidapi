package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.CartController
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.OrderController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.model.order.Cart
import za.co.kasid.kasidrestapi.model.order.PlaceOrderRequest
import za.co.kasid.kasidrestapi.model.order.success.PlaceOrderResponse
import za.co.kasid.kasidrestapi.service.CartService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object OrderConfirmed {

    fun manageOrderSelection(sessionService: SessionService, userMessage: UserMessage): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            when (StepUtil.extractNumbers(response).toInt()) {
                1 -> OrderDetails.sendOrderDetailsMessage()
                2 -> {
                    MainMenu.createMainMenuMessage(userMessage)
                    SessionController(sessionService).updateSession(session?.id!!, session.apply { this.sessionStep = StepEnumV2.MAIN_MENU_PAGE })
                }
                else -> MessageController().sendMessage(MessageUtil.createWhatsAppMessage("Invalid Selection ⭕", userMessage))
            }
            return "End"
        } else {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(MainMenu.messageFormatError(), userMessage))
        }
        when {
            response.contains("*") -> {
                if (session?.id != null) {
                    MessageController().sendMessage(MessageUtil.createWhatsAppMessage("⭕ Your session has been cancelled ⭕", userMessage))
                    SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = StepEnumV2.SESSION_EXPIRED })
                }
            }
        }
        return "End"
    }

    fun confirmCashOnDelivery(sessionService: SessionService, session: WhatsAppSession?, cartService: CartService) {
        session?.apply {
            sessionStep = StepEnumV2.ORDER_CONFIRMED
            paymentMethod = "Cash"
            orderId = createOrder(session, cartService, "cash_on_delivery")?.order_id
            itemPrice = getCartTotal(session, cartService)
        }.let { SessionController(sessionService).updateSession(session?.id!!, it!!) }
        MessageController().sendMessage(MessageUtil.createWhatsAppMessageWithoutResponse(createSuccessfulPaymentMessage(), session?.userNumber))
    }

    fun confirmCard(sessionService: SessionService, session: WhatsAppSession?, cartService: CartService) {
        session?.apply {
            sessionStep = StepEnumV2.ORDER_CONFIRMED
            paymentMethod = "Card"
            orderId = createOrder(session, cartService, "digital_payment")?.order_id
            itemPrice = getCartTotal(session, cartService)
        }.let { SessionController(sessionService).updateSession(session?.id!!, it!!) }
        MessageController().sendMessage(MessageUtil.createWhatsAppMessageWithoutResponse(createFlutterwaveMessage(session), session?.userNumber))
    }

    private fun getCartTotal(session: WhatsAppSession?, cartService: CartService): Double {
        val cart = CartController(cartService).getSelectedFoodByReference(session?.reference!!)
        var total = 25.0
        cart.forEach { total += it?.itemPrice!! }
        return total
    }

    private fun createOrder(session: WhatsAppSession?, cartService: CartService, orderType: String): PlaceOrderResponse? {
        val sessionCart = CartController(cartService).getSelectedFoodByReference(session?.reference!!)
        val cartList: ArrayList<Cart> = arrayListOf()
        sessionCart.forEach { selectedFood ->
            cartList.add(Cart().apply {
                food_id = selectedFood?.itemId
                price = selectedFood?.itemPrice.toString()
                quantity = 1
            })
        }
        val placeOrderRequest = PlaceOrderRequest().apply {
            cart = cartList
            order_amount = getCartTotal(session, cartService)
            order_type = "delivery"
            payment_method = orderType
            restaurant_id = session.restaurantId
            distance = session.distance //
            address = session.address
            latitude = session.lat
            longitude = session.long
            contact_person_name = session.userName +" "+ session.userName
            contact_person_number = "+" + session.userNumber
            address_type = "home"
        }
        return OrderController().placeOrder(placeOrderRequest)
    }

    private fun createSuccessfulPaymentMessage(): String {
        val lines: ArrayList<String> = arrayListOf()
        lines.add("✅ Your order has been confirmed. You will pay using Cash on delivery ✅")
        lines.add(" \n")
        lines.add("To track your orders, please select the following options")
        lines.add(" \n")
        lines.add("1" + " - " + "View order details" + "\n")
        lines.add("2" + " - " + "Go back to main menu" + "\n")
        lines.add(" \n")
        lines.add("\uD83D\uDED1 Send * to end this session \uD83D\uDED1" + "\n")
        return lines.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    private fun createSuccessfulCardOrder(): String {
        val lines: ArrayList<String> = arrayListOf()
        lines.add("✅ Your order has been confirmed. You will pay using Card ✅")
        lines.add(" \n")
        lines.add("To track your orders, please select the following options")
        lines.add(" \n")
        lines.add("1" + " - " + "View order details" + "\n")
        lines.add("2" + " - " + "Go back to main menu" + "\n")
        lines.add(" \n")
        lines.add("\uD83D\uDED1 Send * to end this session \uD83D\uDED1" + "\n")
        return lines.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    const val PAYMENT_TEMPLATE = "https://app.kasid.co.za/payment-mobile?customer_id=1&order_id=100849"

    const val BASE_URL_V2 = "https://app.kasid.co.za/payment-mobile"

    private fun createFlutterwaveMessage(session: WhatsAppSession?): String {
        val payments: ArrayList<String> = arrayListOf()
        payments.add("✅ Your order has been confirmed. You will pay using Card ✅")
        payments.add(" \n")
        payments.add("\uD83D\uDE4F Please click on the link below to pay \uD83D\uDC47" + "\n")
        payments.add(" \n")
        payments.add(BASE_URL_V2 + "?customer_id=" + session?.userId + "&order_id=" + session?.orderId)
        payments.add(" \n")
        payments.add("\uD83D\uDED1 Send 1 to switch to Cash on Delivery Instead \uD83D\uDED1" + "\n")
        payments.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return payments.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}