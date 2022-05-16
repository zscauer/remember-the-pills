package ru.tyumentsev.rememberthepillsbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.handlers.CallbackQueryHandler;
import ru.tyumentsev.rememberthepillsbot.handlers.MessageHandler;
import ru.tyumentsev.rememberthepillsbot.service.BotUserService;

/**
 * Processing all requests from user.
 */
@Component
public class BotFacade {

    @Autowired
    private BotUserService botUserService;
    @Autowired
    private CallbackQueryHandler callbackQueryHandler;
    @Autowired
    private MessageHandler messageHandler;

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> replyMessage = null;

        if (update.hasCallbackQuery()) {
            replyMessage = processCallbackQuery(update.getCallbackQuery());
        } else {
            Message inputMessage = update.getMessage();
            if (inputMessage != null && inputMessage.hasText()) {
                replyMessage = processInputMessage(inputMessage);
            }
        }

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final String selectedItem = callbackQuery.getData();
        final BotUser botUser = botUserService.findByUser(callbackQuery.getFrom());

        botUserService.setBotState(botUser, selectedItem);
        botUserService.save(botUser);

        return callbackQueryHandler.replyCallbackQuery(callbackQuery, botUser);
    }

    private BotApiMethod<?> processInputMessage(Message inputMessage) {
        final String messageText = inputMessage.getText();
        final BotUser botUser = botUserService.findByUser(inputMessage.getFrom());

        botUserService.setBotState(botUser, messageText);
        botUserService.save(botUser);

        return messageHandler.replyMessage(inputMessage, botUser);
    }

}
