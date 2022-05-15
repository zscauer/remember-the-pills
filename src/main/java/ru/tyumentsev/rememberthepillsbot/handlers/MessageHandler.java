package ru.tyumentsev.rememberthepillsbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.tyumentsev.rememberthepillsbot.bot.BotStateContext;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

/**
 * Handle every message from user by sending it to bot state context,
 * which should define correct handler by user state.
 * 
 * @see BotStateContext
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {

    @Autowired
    BotStateContext botStateContext;

    public BotApiMethod<?> replyMessage(Message inputMessage, BotUser botUser) {
        
        log.info("New input message from User:{} in chat {} with text: {}",
                inputMessage.getFrom().getUserName(), inputMessage.getChatId(), inputMessage.getText());

        SendMessage replyMessage = botStateContext.proccessInputMessage(botUser, inputMessage);

        return replyMessage;
    }

}
