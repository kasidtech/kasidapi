package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.AddressController
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.RestaurantController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.AddressService
import za.co.kasid.kasidrestapi.service.FoodService
import za.co.kasid.kasidrestapi.service.RestaurantService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object SelectRestaurant {

    fun manageSelectRestaurant(userMessage: UserMessage, sessionService: SessionService, restaurantService: RestaurantService) {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val restaurants = RestaurantController(restaurantService).getRestaurantsByZone(session?.zoneId?.plus(1) ?: 0)
        if (restaurants.isNotEmpty()) {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createRestaurantMessage(restaurants), userMessage))
        }
    }

    fun manageSelectedRestaurant(userMessage: UserMessage, sessionService: SessionService, restaurantService: RestaurantService, foodService: FoodService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val restaurants = RestaurantController(restaurantService).getRestaurantsByZone(session?.zoneId?.plus(1) ?: 0)
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            val responseNumber = StepUtil.extractNumbers(response).toInt()
            restaurants.forEachIndexed { index, restaurant ->
                if (responseNumber == index) {
                    SessionController(sessionService).updateSession(session?.id!!, session.apply {
                        restaurantId = restaurant?.id?.toInt()
                        restaurantName = restaurant?.name
                        sessionStep = StepEnumV2.FOOD_MENU_PAGE
                    })
                    FoodMenu.manageFoodMenu(userMessage, sessionService, foodService)
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
                    SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = StepEnumV2.SESSION_EXPIRED })
                }
            }
        }
        return "End"
    }

    private fun createRestaurantMessage(restaurants: List<Restaurant?>): String {
        val restaurantList: ArrayList<String> = arrayListOf()
        restaurantList.add("\uD83D\uDE4F Please select the restaurant you would like to order from \uD83D\uDC47" + "\n")
        restaurantList.add(" \n")
        restaurants.forEachIndexed { index, zone -> restaurantList.add((index).toString() + " - " + zone?.name + "\n") }
        restaurantList.add(" \n")
        restaurantList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return restaurantList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}