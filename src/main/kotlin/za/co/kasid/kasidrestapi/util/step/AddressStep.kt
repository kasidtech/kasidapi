package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.AddressController
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.OrderController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.enums.StepEnumV2.SELECT_RESTAURANT_PAGE
import za.co.kasid.kasidrestapi.enums.StepEnumV2.SESSION_EXPIRED
import za.co.kasid.kasidrestapi.model.address.CustomerAddress
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.AddressService
import za.co.kasid.kasidrestapi.service.RestaurantService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object AddressStep {

    fun manageAddress(userMessage: UserMessage, sessionService: SessionService, addressService: AddressService) {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val userNumber = "+" + MessageUtil.getUserNumber(userMessage)
        val addresses = AddressController(addressService).getByContactNumber(userNumber)
        if (addresses.isNotEmpty()) {
            if (session?.id != null) SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = StepEnumV2.SELECT_ADDRESS_PAGE })
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createSelectAddressMessage(addresses), userMessage))
        } else {
            if (session?.id != null) SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = StepEnumV2.ADD_ADDRESS_PAGE })
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createAddAddressMessage(), userMessage))
        }
    }

    fun manageAddAddress(userMessage: UserMessage, sessionService: SessionService, restaurantService: RestaurantService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val response = MessageUtil.getUserResponse(userMessage)
        when {
            response.contains("*") -> {
                if (session?.id != null) {
                    MessageController().sendMessage(MessageUtil.createWhatsAppMessage("⭕ Your session has been cancelled ⭕", userMessage))
                    SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = SESSION_EXPIRED })
                    return "End"
                }
            }
            else -> {
                val zones = OrderController().getZones(response)
                if (zones.zone_data.isNotEmpty()) {
                    SessionController(sessionService).updateSession(session?.id!!, session.apply {
                        address = response
                        zoneId = zones.zone_data.first().id
                        sessionStep = SELECT_RESTAURANT_PAGE
                        userId = 1
                        lat = zones.lat
                        long = zones.long
                    })
                    SelectRestaurant.manageSelectRestaurant(userMessage, sessionService, restaurantService)
                }
            }
        }
        //Process adding a new address
        return "End"
    }

    fun manageSelectAddress(userMessage: UserMessage, sessionService: SessionService, addressService: AddressService, restaurantService: RestaurantService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val userNumber = "+" + MessageUtil.getUserNumber(userMessage)
        val addresses = AddressController(addressService).getByContactNumber(userNumber)
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            val responseNumber = StepUtil.extractNumbers(response).toInt()
            addresses.forEachIndexed { index, customerAddress ->
                if (responseNumber == index) {
                    SessionController(sessionService).updateSession(session?.id!!, session.apply {
                        address = customerAddress?.address
                        zoneId = customerAddress?.zoneid
                        sessionStep = SELECT_RESTAURANT_PAGE
                        userId = customerAddress?.userid
                        lat = customerAddress?.latitude
                        long = customerAddress?.longitude
                    })
                    SelectRestaurant.manageSelectRestaurant(userMessage, sessionService, restaurantService)
                    return "End"
                }
            }
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage("Invalid Selection ⭕", userMessage))
            return "End"
        } else {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(MainMenu.messageFormatError(), userMessage))
        }
        when {
            response.contains("*") -> {
                if (session?.id != null) {
                    MessageController().sendMessage(MessageUtil.createWhatsAppMessage("⭕ Your session has been cancelled ⭕", userMessage))
                    SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = SESSION_EXPIRED })
                }
            }
        }
        return "End"
    }

    private fun createSelectAddressMessage(addresses: List<CustomerAddress?>): String {
        val addressList: ArrayList<String> = arrayListOf()
        addressList.add("We noticed that you have an account with KasiD. Please select an address to continue. Only reply with the number " + "\n")
        addressList.add(" \n")
        addresses.forEachIndexed { index, address -> addressList.add((index).toString() + " - " + address?.address + "\n") }
        addressList.add(" \n")
        addressList.add("If you want to add a new address please reply with #" + "\n")
        addressList.add(" \n")
        addressList.add("\uD83D\uDED1 Send * to cancel this session \uD83D\uDED1" + "\n")
        return addressList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    private fun createAddAddressMessage(): String {
        val addressList: ArrayList<String> = arrayListOf()
        addressList.add("Please type in your address to continue. For example, type in 50 Molambo Street, Ibazelo Section, Thembisa" + "\n")
        addressList.add(" \n")
        addressList.add("\uD83D\uDED1 Send * to cancel this session \uD83D\uDED1" + "\n")
        return addressList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}