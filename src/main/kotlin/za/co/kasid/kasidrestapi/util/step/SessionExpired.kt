package za.co.kasid.kasidrestapi.util.step

import za.co.kasid.kasidrestapi.controller.SessionController
import za.co.kasid.kasidrestapi.model.message_recieved.UserMessage
import za.co.kasid.kasidrestapi.service.SessionService
import za.co.kasid.kasidrestapi.util.StepHandler

object SessionExpired {

    fun handleSessionExpired(userMessage: UserMessage, sessionService: SessionService) {
        SessionController(sessionService).saveSession(StepHandler.createNewSession(userMessage))
        MainMenu.createMainMenuMessage(userMessage)
    }
}