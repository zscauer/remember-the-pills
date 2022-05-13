package ru.tyumentsev.remember_the_pills.handlers;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.entity.BotUser;

public interface UserRequestHandler {
    SendMessage handle(BotUser botUser, BotApiObject botApiObject);

    BotState getHandlerName();
}
