package ru.tyumentsev.rememberthepillsbot.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.tyumentsev.rememberthepillsbot.bot.BotStateContext;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

/**
 * Handle every callback from user by sending it to bot state context,
 * which should define correct handler by user state.
 * 
 * @see BotStateContext
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler {

    @Autowired
    BotStateContext botStateContext;

    public BotApiMethod<?> replyCallbackQuery(CallbackQuery callbackQuery, BotUser botUser) {

        log.info("New input callback message from User:{} in chat {} with text: {}",
                callbackQuery.getFrom().getUserName(), callbackQuery.getChatInstance(), callbackQuery.getData());

        BotApiMethod<?> answerCallbackQuery = botStateContext.proccessCallbackQuery(botUser, callbackQuery);

        return answerCallbackQuery;
    }
}
