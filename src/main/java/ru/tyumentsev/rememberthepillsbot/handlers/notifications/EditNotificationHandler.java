package ru.tyumentsev.rememberthepillsbot.handlers.notifications;

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
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.BotStateContext;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;
import ru.tyumentsev.rememberthepillsbot.cache.NotificationCache;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.BotUserService;
import ru.tyumentsev.rememberthepillsbot.service.ReplyMessageService;

/**
 * Handle user request of edit notification.
 * 
 * @see UserRequestHandler
 * @see ReplyMessageService
 * @see NotificationCache
 */
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
        BotStateContext botStateContext = applicationContext.getBean("botStateContext", BotStateContext.class);

        // get id of notification that user wants to delete.
        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            // get id of notification that user wants to delete.
            chatId = callbackQuery.getMessage().getChatId();
            notificationId = Long.parseLong(callbackQuery.getData().split(":")[1]);
            // save requested item's id to find it later by chat id.
            notificationCache.put(chatId, notificationId);

            return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                    RemindItemState.NOTIFICATION_ADD_TIME);
        } else { // if it's not CallbackQuery, that means user send new time of notification.
            Message message = (Message) botApiObject;
            String userAnswer = message.getText();
            chatId = message.getChatId();
            notificationId = notificationCache.take(chatId);
            // find selected previously notification by chat id.
            RoutineNotification routineNotification = botUser.getRemindItems().stream()
                    .flatMap(notifications -> notifications.getNotifications().stream())
                    .filter(notification -> notification.getId() == notificationId).findFirst().get();

            routineNotification.setNotificationTime(new Time(RoutineNotification.getTimeLongValue(userAnswer)));

            botUser.setBotState(BotState.ITEM_EDIT);
            botUserService.save(botUser);
            message.setText(String.valueOf(routineNotification.getRemindItem().getId()));

            return botStateContext.proccessInputMessage(botUser, message);
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_EDIT_NOTIFICATION;
    }

}
