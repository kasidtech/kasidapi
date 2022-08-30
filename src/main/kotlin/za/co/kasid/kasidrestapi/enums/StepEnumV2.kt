package za.co.kasid.kasidrestapi.enums

enum class StepEnumV2 {
    NEW_SESSION,
    MAIN_MENU_PAGE, // Show main menu. Place New Order, View Previous Orders, View Order Details, Chat To An Agent
    VIEW_PREVIOUS_ORDERS_PAGE, // Show list of all customer orders. Customer can select an order to view more
    ORDER_DETAILS_PAGE, // Show details of a selected order
    AGENT_CHAT_PAGE, // No automated responses in this mode.
    SELECT_ADDRESS_PAGE, // Point where we create an order. If user has addresses we this page first
    ADD_ADDRESS_PAGE, // Point where we create an order. If user does not have an address we show this page second
    SELECT_RESTAURANT_PAGE, // User can select a restaurant
    FOOD_MENU_PAGE, // Where we show the restaurant menu and what's in the cart currently
    CHECKOUT_PAGE, // Where a user can select a payment method
    //AWAITING_CARD_PAYMENT_PAGE, // Waiting for user to finish paying by card
    //PAYMENT_FAILED, //Payment failed step
    ORDER_CONFIRMED, // Show order details
    SESSION_EXPIRED, // Session expired and no more activity can be completed
}