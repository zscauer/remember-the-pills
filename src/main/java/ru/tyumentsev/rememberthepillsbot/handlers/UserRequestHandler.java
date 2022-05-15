package ru.tyumentsev.rememberthepillsbot.handlers;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ru.tyumentsev.rememberthepillsbot.bot.States;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

/**
 * Handle requests based on user state.
 * 
 * @see BotStateContext#messageHandlers
 */
public interface UserRequestHandler {
    /**
     * Get request and handle it based on user state.
     * 
     * @param botUser       instance of registered in database user
     * @param botApiObject  request from user
     * @return              result message to user
     */
    SendMessage handle(BotUser botUser, BotApiObject botApiObject);

    /**
     * Return state that define a purpose of handler.
     * 
     * @return value, that corresponds to current handler purpose
     */
    States getHandlerName();

}
