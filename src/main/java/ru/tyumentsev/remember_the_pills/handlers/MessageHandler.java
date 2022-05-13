package ru.tyumentsev.remember_the_pills.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.tyumentsev.remember_the_pills.bot.BotStateContext;
import ru.tyumentsev.remember_the_pills.entity.BotUser;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {

    @Autowired
    BotStateContext botStateContext;

    public BotApiMethod<?> replyMessage(Message inputMessage, BotUser botUser) {
        final String inputMessageText = inputMessage.getText();
        final String userName = inputMessage.getFrom().getUserName();
        final long chatId = inputMessage.getChatId();

        log.info("New input message from User:{} in chat {} with text: {}",
                userName, chatId, inputMessageText);

        SendMessage replyMessage = botStateContext.proccessInputMessage(botUser, inputMessage);

        return replyMessage;
    }

}
