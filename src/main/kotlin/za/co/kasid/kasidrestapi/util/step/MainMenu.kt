package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.SessionStepEnum
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.AddressService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.MessageUtil.getUserNumber
import za.co.kasid.kasidrestapi.util.MessageUtil.getUserResponse
import za.co.kasid.kasidrestapi.util.StepUtil

object MainMenu {

    fun mainMenu(userMessage: UserMessage, sessionService: SessionService, addressService: AddressService): Any {
        val session = sessionService.getByNumber(getUserNumber(userMessage)).lastOrNull()
        val response = getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            when (StepUtil.extractNumbers(response).toInt()) {
                1 -> AddressStep.manageAddress(userMessage, sessionService, addressService) //Create order
                2 -> ViewPreviousOrders.showPreviousOrders(userMessage, sessionService) //View previous orders
                3 -> AgentChat.chatToAnAgent(userMessage, sessionService) //Chat to agent
                else -> MessageController().sendMessage(MessageUtil.createWhatsAppMessage("Invalid Selection ⭕", userMessage))
            }
            return "End"
        } else {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(messageFormatError(), userMessage))
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

    fun createMainMenuMessage(userMessage: UserMessage) {
        val menuList: ArrayList<String> = arrayListOf()
        menuList.add("✨ Hey there. Welcome to the KasiD self help WhatsApp portal. ✨" + "\n")
        menuList.add("\uD83D\uDE4F Please select an option to get started \uD83D\uDC47" + "\n")
        menuList.add(" \n")
        menuList.add("1" + " - " + "Place An Order \uD83D\uDCB5" + "\n")
        menuList.add("2" + " - " + "View previous orders \uD83D\uDCB3" + "\n")
        menuList.add("3" + " - " + "Chat to an agent \uD83D\uDCB3" + "\n")
        menuList.add(" \n")
        menuList.add("\uD83D\uDED1 Send * to end this session \uD83D\uDED1" + "\n")
        val menu = menuList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
        MessageController().sendMessage(MessageUtil.createWhatsAppMessage(menu, userMessage))
    }

    fun messageFormatError(): String {
        return "Invalid message. Please reply with a number only. If you want to cancel this session reply with *"
    }
}