package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.CartController
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.SessionStepEnum
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.SelectedFood
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.flutterwave.WebhookPaymentResponse
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.CartService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object AwaitingCardPayment {

    private const val PAYMENT_SUCCESS = "successful"

    fun manageCreateCardPayment(userMessage: UserMessage, sessionService: SessionService, session: WhatsAppSession?, cartService: CartService) {
        session?.apply {
            sessionStep = StepEnumV2.ORDER_CONFIRMED
            paymentMethod = "Card"
        }?.let { SessionController(sessionService).updateSession(session.id!!, it) }
        val cart = CartController(cartService).getSelectedFoodByReference(session?.reference!!)
        MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createSuccessfulPaymentMessage(cart), userMessage))
    }

    fun confirmOrder(paymentResponse: WebhookPaymentResponse?, sessionService: SessionService) {
        val paymentSession = sessionService.getByReference(paymentResponse?.data?.tx_ref)
        if (paymentResponse?.data?.status == PAYMENT_SUCCESS) {
            updateSession(SessionController(sessionService), paymentSession, StepEnumV2.ORDER_CONFIRMED)
            //MessageController().sendMessage(MessageUtil.createWhatsAppMessageWithoutResponse(createSuccessfulPaymentMessage(), paymentSession.userNumber))
        } else {
            //updateSession(SessionController(sessionService), paymentSession, StepEnumV2.PAYMENT_FAILED)
            MessageController().sendMessage(MessageUtil.createWhatsAppMessageWithoutResponse(createFailedPaymentMessage(), paymentSession.userNumber))
        }
    }

    fun paymentNotComplete(sessionService: SessionService, userMessage: UserMessage, cartService: CartService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            when (StepUtil.extractNumbers(response).toInt()) {
                1 -> OrderConfirmed.confirmCashOnDelivery(sessionService, session, cartService)
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

    fun paymentFailed(sessionService: SessionService, userMessage: UserMessage, cartService: CartService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            when (StepUtil.extractNumbers(response).toInt()) {
                1 -> manageCreateCardPayment(userMessage, sessionService, session, cartService)
                2 -> OrderConfirmed.confirmCashOnDelivery(sessionService, session, cartService)
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

    fun updateSession(sessionController: SessionController, session: WhatsAppSession?, sessionStepEnumV2: StepEnumV2) {
        session?.apply { sessionStep = sessionStepEnumV2 }?.let { sessionController.updateSession(session.id!!, it) }
    }

    private fun createSuccessfulPaymentMessage(cart: List<SelectedFood?>): String {
        val lines: ArrayList<String> = arrayListOf()
        lines.add("✅ Your order has been confirmed. You paid using your card ✅")
        lines.add(" \n")
        lines.add("To track your orders, please select the following options")
        lines.add(" \n")
        lines.add("1" + " - " + "View order details" + "\n")
        lines.add("2" + " - " + "Go back to main menu" + "\n")
        lines.add(" \n")
        lines.add("\uD83D\uDED1 Send * to end this session \uD83D\uDED1" + "\n")
        return lines.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    private fun createFailedPaymentMessage(): String {
        val lines: ArrayList<String> = arrayListOf()
        lines.add("⭕ Your payment has failed ⭕")
        lines.add(" \n")
        lines.add("Please select a payment option below")
        lines.add(" \n")
        lines.add("1" + " - " + "Retry Card Payment" + "\n")
        lines.add("2" + " - " + "Switch to Cash On Delivery" + "\n")
        lines.add(" \n")
        lines.add("\uD83D\uDED1 Send * to end this session \uD83D\uDED1" + "\n")
        return lines.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    private fun createFlutterwaveMessage(session: WhatsAppSession?): String {
        val payments: ArrayList<String> = arrayListOf()
        payments.add("\uD83D\uDE4F Please click on the link below to pay. Please note that your order will only be confirmed once you have paid \uD83D\uDC47" + "\n")
        payments.add(" \n")
        payments.add("""${StepUtil.BASE_URL}?amount=${session?.itemPrice}&email=kasid@open-technik.co.za&name=${session?.userName}&ref=${session?.reference}""")
        payments.add(" \n")
        payments.add("\uD83D\uDED1 Send 1 to switch to Cash on Delivery Instead \uD83D\uDED1" + "\n")
        payments.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return payments.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}