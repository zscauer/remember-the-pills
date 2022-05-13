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
public class DeleteNotificationHandler implements UserRequestHandler {
    
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    NotificationCache notificationCache;
    @Autowired
    BotUserService botUserService;
    @Autowired
    RemindItemService remindItemService;
    @Autowired
    ApplicationContext applicationContext;
    
    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        long notificationId;
        
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        notificationId = Long.parseLong(callbackQuery.getData().split(":")[1]);

        RoutineNotification routineNotification = botUser.getRemindItems().stream()
                    .flatMap(notifications -> notifications.getNotifications().stream())
                    .filter(notification -> notification.getId() == notificationId).findFirst().get();
        
        RemindItem remindItem = routineNotification.getRemindItem();
        remindItem.getNotifications().remove(routineNotification);
        // remindItem = remindItemService.saveItem(remindItem);
        

        botUser.setBotState(BotState.ITEM_EDIT);
        botUserService.save(botUser);
        Message message = callbackQuery.getMessage();
        message.setText(String.valueOf(remindItem.getId()));

        BotStateContext botStateContext = applicationContext.getBean("botStateContext", BotStateContext.class);
            
        return botStateContext.proccessInputMessage(botUser, message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_DELETE_NOTIFICATION;
    }
    
}
