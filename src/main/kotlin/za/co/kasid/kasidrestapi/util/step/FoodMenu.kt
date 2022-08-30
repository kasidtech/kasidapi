package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.CartController
import za.co.kasid.kasidrestapi.controller.FoodController
import za.co.kasid.kasidrestapi.controller.MessageController
import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.enums.StepEnumV2
import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.SelectedFood
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.CartService
import za.co.kasid.kasidrestapi.service.FoodService
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.MessageUtil
import za.co.kasid.kasidrestapi.util.StepUtil

object FoodMenu {

    fun manageFoodMenu(userMessage: UserMessage, sessionService: SessionService, foodService: FoodService) {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val menu = FoodController(foodService).getByRestaurantId(session?.restaurantId!!)
        if (menu.isNotEmpty()) {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createFoodMessage(menu), userMessage))
        }
    }

    fun manageFoodMenu2(userMessage: UserMessage, sessionService: SessionService, foodService: FoodService) {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val menu = FoodController(foodService).getByRestaurantId(session?.restaurantId!!)
        if (menu.isNotEmpty()) {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(createSecondFoodMessage(), userMessage))
        }
    }

    fun manageSelectedFood(userMessage: UserMessage, sessionService: SessionService, foodService: FoodService, cartService: CartService): Any {
        val session = sessionService.getByNumber(MessageUtil.getUserNumber(userMessage)).lastOrNull()
        val menu = FoodController(foodService).getByRestaurantId(session?.restaurantId!!)
        val response = MessageUtil.getUserResponse(userMessage)
        if (StepUtil.isNumeric(response)) {
            val responseNumber = StepUtil.extractNumbers(response).toInt()
            menu.forEachIndexed { index, food ->
                if (responseNumber == index) {
                    CartController(cartService).saveSelectedFood(SelectedFood().apply {
                        itemId = food?.id?.toInt()
                        itemName = food?.name
                        itemPrice = food?.price
                        reference = session.reference
                    })
                    manageFoodMenu2(userMessage, sessionService, foodService)
                    return "End"
                }
                MessageController().sendMessage(MessageUtil.createWhatsAppMessage("Invalid Selection ⭕", userMessage))
                return "End"
            }
        } else if (response.contains("#")) {
            val cartList = CartController(cartService).getSelectedFoodByReference(session.reference)
            var total = 25.0
            cartList.forEach { total += it?.itemPrice!! }
            SessionController(sessionService).updateSession(session.id!!, session.apply {
                sessionStep = StepEnumV2.CHECKOUT_PAGE
                itemPrice = total
            })
            Checkout.manageCheckout(userMessage, sessionService, foodService, cartService)
            return "End"
        } else {
            MessageController().sendMessage(MessageUtil.createWhatsAppMessage(MainMenu.messageFormatError(), userMessage))
        }
        when {
            response.contains("*") -> {
                if (session.id != null) {
                    MessageController().sendMessage(MessageUtil.createWhatsAppMessage("⭕ Your session has been cancelled ⭕", userMessage))
                    SessionController(sessionService).updateSession(session.id!!, session.apply { sessionStep = StepEnumV2.SESSION_EXPIRED })
                }
            }
        }
        return "End"
    }

    private fun createFoodMessage(foods: List<FoodItem?>): String {
        val foodList: ArrayList<String> = arrayListOf()
        foodList.add("\uD83D\uDE4F Please select the item you would like to order and add it to your cart \uD83D\uDC47" + "\n")
        foodList.add(" \n")
        foods.forEachIndexed { index, food -> foodList.add((index).toString() + " - " + food?.name + "\n") }
        foodList.add(" \n")
        foodList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return foodList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    private fun createSecondFoodMessage(): String {
        val foodList: ArrayList<String> = arrayListOf()
        foodList.add("✅ Added to cart. If you are done adding to your cart, please send # ✅" + "\n")
        foodList.add(" \n")
        foodList.add(" If you want to add more items to your cart please send a number of the item you would like to add " + "\n")
        foodList.add(" \n")
        foodList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return foodList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

}