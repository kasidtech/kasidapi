package za.co.kasid.kasidrestapi.util

import za.co.kasid.kasidrestapi.model.FoodItem
import za.co.kasid.kasidrestapi.model.Restaurant
import za.co.kasid.kasidrestapi.model.WhatsAppSession
import za.co.kasid.kasidrestapi.model.Zone
import java.util.regex.Pattern

object StepUtil {

    const val BASE_URL = "https://whatsapppay.kasid.co.za/index.php"

    fun createFirstMessage(): String {
        val menuList: ArrayList<String> = arrayListOf()
        menuList.add("✨ Hey there. Welcome to the KasiD self help WhatsApp portal. ✨" + "\n")
        menuList.add("\uD83D\uDE4F Please select an item to get started \uD83D\uDC47" + "\n")
        menuList.add(" \n")
        menuList.add("1" + " - " + "Place An Order \uD83D\uDCB5" + "\n")
        menuList.add("2" + " - " + "Get Help \uD83D\uDCB3" + "\n")
        menuList.add(" \n")
        menuList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        return menuList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createZoneMessage(zones: List<Zone>): String {
        val zoneList: ArrayList<String> = arrayListOf()
        zoneList.add("\uD83D\uDE4F Please select the zone where your order will be delivered \uD83D\uDC47" + "\n")
        zoneList.add(" \n")
        zones.forEachIndexed { index, zone -> zoneList.add((index+1).toString() + " - " + zone.name + "\n") }
        zoneList.add(" \n")
        zoneList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        zoneList.add("\uD83C\uDF0E Send # for help \uD83C\uDF0E" + "\n")
        return zoneList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createRestaurantMessage(restaurants: List<Restaurant>): String {
        val restaurantList: ArrayList<String> = arrayListOf()
        restaurantList.add("\uD83D\uDE4F Please select the restaurant you would like to order from \uD83D\uDC47" + "\n")
        restaurantList.add(" \n")
        restaurants.forEachIndexed { index, zone -> restaurantList.add((index+2).toString() + " - " + zone.name + "\n") }
        restaurantList.add(" \n")
        restaurantList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        restaurantList.add("\uD83C\uDF0E Send # for help \uD83C\uDF0E" + "\n")
        return restaurantList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createFoodMessage(foods: List<FoodItem?>): String {
        val foodList: ArrayList<String> = arrayListOf()
        foodList.add("\uD83D\uDE4F Please select the item you would like to order \uD83D\uDC47" + "\n")
        foodList.add(" \n")
        foods.forEachIndexed { index, zone -> foodList.add((index+2).toString() + " - " + zone?.name + "\n") }
        foodList.add(" \n")
        foodList.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        foodList.add("\uD83C\uDF0E Send # for help \uD83C\uDF0E" + "\n")
        return foodList.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createPaymentMessage(): String {
        val payments: ArrayList<String> = arrayListOf()
        payments.add("\uD83D\uDE4F Please select the payment type for your order \uD83D\uDC47" + "\n")
        payments.add(" \n")
        payments.add("1" + " - " + "Cash On Delivery \uD83D\uDCB5" + "\n")
        payments.add("2" + " - " + "Card Payment \uD83D\uDCB3" + "\n")
        payments.add(" \n")
        payments.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        payments.add("\uD83C\uDF0E Send # for help \uD83C\uDF0E" + "\n")
        return payments.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createFlutterwaveMessage(session: WhatsAppSession): String {
        val payments: ArrayList<String> = arrayListOf()
        payments.add("\uD83D\uDE4F Please click on the link below to pay. Please note that your order will only be confirmed once you have paid \uD83D\uDC47" + "\n")
        payments.add(" \n")
        payments.add("""$BASE_URL?amount=${session.itemPrice}&email=${session.emailAddress}&name=${session.userName}&ref=${session.id}${session.sessionUnixTimeStamp}""")
        payments.add(" \n")
        payments.add("\uD83D\uDED1 Send 1 to switch to Cash on Delivery Instead \uD83D\uDED1" + "\n")
        payments.add("\uD83D\uDED1 Send * to cancel your order and this session \uD83D\uDED1" + "\n")
        payments.add("\uD83C\uDF0E Send # for help \uD83C\uDF0E" + "\n")
        return payments.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createHelpMessage(): String {
        val payments: ArrayList<String> = arrayListOf()
        payments.add("\uD83D\uDE4F What would you like help with? \uD83D\uDC47" + "\n")
        payments.add(" \n")
        payments.add("1" + " - " + "My order is late" + "\n")
        payments.add("2" + " - " + "Refund" + "\n")
        payments.add("2" + " - " + "Something is wrong with my order" + "\n")
        payments.add(" \n")
        payments.add("\uD83D\uDED1 Send * to cancel this session \uD83D\uDED1" + "\n")
        return payments.toString().replace("[", "").replace("]", "").replace(",", "").trim()
    }

    fun createSessionExpiredMessage(): String {
        return "Your previous session has expired. Starting a new one"
    }

    fun isNumeric(message: String): Boolean {
        return message.chars().allMatch { x -> Character.isDigit(x) }
    }

    fun hasNumbers(message: String): Boolean {
        val regex = "(.)*(\\d)(.)*"
        val pattern: Pattern = Pattern.compile(regex)
        return pattern.matcher(message).matches() || message.contains("*") || message.contains("#")
    }

    fun extractNumbers(message: String): Long {
        val numberOnly = message.replace("[^0-9]".toRegex(), "")
        return numberOnly.toLong()
    }

}