package ru.tyumentsev.rememberthepillsbot.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;

/**
 * Provides access to all handlers and calls the necessary.
 * 
 * @see UserRequestHandler
 * @see States
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotStateContext {
    
    // keeps all handlers to call needed later depending on user state
    Map<States, UserRequestHandler> messageHandlers = new HashMap<>();
    
    public BotStateContext(List<UserRequestHandler> userRequestHandlers) {
        userRequestHandlers.forEach(handler -> messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage proccessCallbackQuery(BotUser botUser, CallbackQuery callbackQuery) {
        UserRequestHandler userRequestHandler = messageHandlers.get(botUser.getBotState());
        return userRequestHandler.handle(botUser, callbackQuery);
    }

    public SendMessage proccessInputMessage(BotUser botUser, Message message) {
        UserRequestHandler userRequestHandler = messageHandlers.get(botUser.getBotState());
        return userRequestHandler.handle(botUser, message);
    }
}

   
