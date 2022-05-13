package ru.tyumentsev.remember_the_pills.bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.handlers.UserRequestHandler;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotStateContext {
    
    Map<BotState, UserRequestHandler> messageHandlers = new HashMap<>();
    
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

   
