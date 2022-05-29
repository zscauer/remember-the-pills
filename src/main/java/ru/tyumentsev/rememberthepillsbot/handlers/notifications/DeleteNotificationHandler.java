package ru.tyumentsev.rememberthepillsbot.handlers.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.BotStateContext;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.BotUserService;

/**
 * Handle user request of delete notification.
 * 
 * @see UserRequestHandler
 * @see MenuService
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeleteNotificationHandler implements UserRequestHandler {

    @Autowired
    BotUserService botUserService;
    @Autowired
    @Lazy
    BotStateContext botStateContext;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {

        long notificationId;  
        //BotStateContext botStateContext = applicationContext.getBean("botStateContext", BotStateContext.class);

        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        // get id of notification that user wants to delete.
        notificationId = Long.parseLong(callbackQuery.getData().split(":")[1]);

        // get user notification by its id.
        RoutineNotification routineNotification = botUser.getRemindItems().stream()
                .flatMap(notifications -> notifications.getNotifications().stream())
                .filter(notification -> notification.getId() == notificationId).findFirst().get();

        RemindItem remindItem = routineNotification.getRemindItem();
        remindItem.getNotifications().remove(routineNotification);

        botUser.setBotState(BotState.ITEM_EDIT);
        botUserService.save(botUser);
        Message message = callbackQuery.getMessage();
        message.setText(String.valueOf(remindItem.getId()));

        return botStateContext.proccessInputMessage(botUser, message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_DELETE_NOTIFICATION;
    }

}
