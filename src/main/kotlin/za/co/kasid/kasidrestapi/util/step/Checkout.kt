package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.*
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.SelectedFood
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.*
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object Checkout {

    fun manageCheckout(userMessage: UserMessage, sessionService: SessionService, foodService: FoodService, cartService: CartService) {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val cart = CartController(cartService).getSelectedFoodByReference(session?.reference!!)
        MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createPaymentMessage(session, cart), userMessage))
    }

    fun manageSelectedCheckout(userMessage: UserMessage, sessionService: SessionService, cartService: CartService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            when (StepUtil.extractNumbers(response).toInt()) {
                1 -> OrderConfirmed.confirmCashOnDelivery(sessionService, session, cartService) //Confirm COD
                2 -> OrderConfirmed.confirmCard(sessionService, session, cartService) //Create Card Payment
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

    private fun createPaymentMessage(session: WhatsAppSession, cartList: List<SelectedFood?>): String {
        var total = 25.0
        cartList.forEach { total += it?.itemPrice!! }
        val checkout: ArrayList<String> = arrayListOf()
        checkout.add("✨ Order Details ✨" + "\n")
        checkout.add(" \n")
        cartList.forEachIndexed { index, cart -> checkout.add((index+1).toString() + " - " + cart?.itemName + "\n") }
        checkout.add(" \n")
        checkout.add("Delivery Address: ${session.address}" + "\n")
        checkout.add(" \n")
        checkout.add("Delivery Fee: R25" + "\n")
        checkout.add("Total: $total" + "\n")
        checkout.add("\uD83D\uDE4F Please select the payment type for your order \uD83D\uDC47" + "\n")
        checkout.add(" \n")
        checkout.add("1" + " - " + "Cash On Delivery \uD83D\uDCB5" + "\n")
        checkout.add("2" + " - " + "Card Payment \uD83D\uDCB3" + "\n")
        checkout.add(" \n")
        checkout.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return checkout.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}