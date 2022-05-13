package ru.tyumentsev.remember_the_pills.handlers.notifications;

import java.sql.Time;

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
import ru.tyumentsev.remember_the_pills.entity.RoutineNotification;
import ru.tyumentsev.remember_the_pills.handlers.UserRequestHandler;
import ru.tyumentsev.remember_the_pills.service.BotUserService;
import ru.tyumentsev.remember_the_pills.service.ReplyMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EditNotificationHandler implements UserRequestHandler {

    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    NotificationCache notificationCache;
    @Autowired
    BotUserService botUserService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        long chatId;
        long notificationId;
        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            chatId = callbackQuery.getMessage().getChatId();
            notificationId = Long.parseLong(callbackQuery.getData().split(":")[1]);
            // save selected item to find it by user id
            notificationCache.put(chatId, notificationId);
            
            return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                    RemindItemState.NOTIFICATION_ADD_TIME);
        } else {
            Message message = (Message) botApiObject;
            String userAnswer = message.getText();
            chatId = message.getChatId();
            notificationId = notificationCache.take(chatId);
            // find selected previously notification by user id
            RoutineNotification routineNotification = botUser.getRemindItems().stream()
                    .flatMap(notifications -> notifications.getNotifications().stream())
                    .filter(notification -> notification.getId() == notificationId).findFirst().get();

            routineNotification.setNotificationTime(new Time(RoutineNotification.getTimeLongValue(userAnswer)));
           
            botUser.setBotState(BotState.ITEM_EDIT);
            botUserService.save(botUser);
            message.setText(String.valueOf(routineNotification.getRemindItem().getId()));
            BotStateContext botStateContext = applicationContext.getBean("botStateContext", BotStateContext.class);
            
            return botStateContext.proccessInputMessage(botUser, message);
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_EDIT_NOTIFICATION;
    }

}
