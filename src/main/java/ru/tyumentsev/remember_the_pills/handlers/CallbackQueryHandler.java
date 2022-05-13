package ru.tyumentsev.remember_the_pills.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.tyumentsev.remember_the_pills.bot.BotStateContext;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.service.MenuService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler {

    @Autowired
    private MenuService menuService;
    @Autowired
    BotStateContext botStateContext;

    public BotApiMethod<?> replyCallbackQuery(CallbackQuery callbackQuery, BotUser botUser) {
        final String inputQuery = callbackQuery.getData();
        final String userName = callbackQuery.getFrom().getUserName();
        final String chatId = callbackQuery.getChatInstance();

        BotApiMethod<?> answerCallbackQuery = botStateContext.proccessCallbackQuery(botUser, callbackQuery);

        log.info("New input callback message from User:{} in chat {} with text: {}",
                userName, chatId, inputQuery);

        // if (userState == BotState.DELETE_ALL_ITEMS) {
        //     answerCallbackQuery = buildCallBackAnswer(callbackQuery.getId(), true,
        //     "Hello dude. This is callback query. Your state is: " + userState.toString());
        // }

        return answerCallbackQuery;
    }

    // private BotApiMethod<?> buildCallBackAnswer(String callBackId, boolean showAlert, String text) {

    //     AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
    //             .callbackQueryId(callBackId)
    //             .showAlert(showAlert)
    //             .text(text)
    //             .build();

    //     return answerCallbackQuery;
    // }

}
