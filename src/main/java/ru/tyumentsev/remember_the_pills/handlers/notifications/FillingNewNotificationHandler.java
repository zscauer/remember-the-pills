package ru.tyumentsev.remember_the_pills.handlers.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.bot.BotStateContext;
import ru.tyumentsev.remember_the_pills.bot.RemindItemState;
import ru.tyumentsev.remember_the_pills.cache.NotificationCache;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.entity.RemindItem;
import ru.tyumentsev.remember_the_pills.entity.RoutineNotification;
import ru.tyumentsev.remember_the_pills.handlers.UserRequestHandler;
import ru.tyumentsev.remember_the_pills.service.BotUserService;
import ru.tyumentsev.remember_the_pills.service.RemindItemService;
import ru.tyumentsev.remember_the_pills.service.ReplyMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FillingNewNotificationHandler implements UserRequestHandler {

    @Autowired
    RemindItemService remindItemService;
    @Autowired
    BotUserService botUserService;
    @Autowired
    NotificationCache notificationCache;
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        long chatId;
        long itemId;

        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            chatId = callbackQuery.getMessage().getChatId();
            itemId = Long.parseLong(callbackQuery.getData().split(":")[1]);
            notificationCache.put(chatId, itemId);
            return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                    RemindItemState.NOTIFICATION_ADD_TIME);
        } else {
            Message message = (Message) botApiObject;
            String userAnswer = message.getText();
            chatId = message.getChatId();
            itemId = notificationCache.take(chatId);
            RemindItem remindItem = botUser.getRemindItems().stream().filter(item -> item.getId() == itemId).findFirst()
                    .get();
            remindItem.addNotification(new RoutineNotification(RoutineNotification.getTimeLongValue(userAnswer)));
            botUser.setBotState(BotState.ITEM_EDIT);
            botUserService.save(botUser);
            message.setText(String.valueOf(itemId));
            BotStateContext botStateContext = applicationContext.getBean("botStateContext", BotStateContext.class);
            return botStateContext.proccessInputMessage(botUser, message);
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_ADD_NOTIFICATION;
    }

}
