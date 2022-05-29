package ru.tyumentsev.rememberthepillsbot.handlers.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.BotStateContext;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;
import ru.tyumentsev.rememberthepillsbot.cache.NotificationCache;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.BotUserService;
import ru.tyumentsev.rememberthepillsbot.service.ReplyMessageService;

/**
 * Handle user request of creating new notification.
 * 
 * @see UserRequestHandler
 * @see ReplyMessageService
 * @see NotificationCache
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
public class FillingNewNotificationHandler implements UserRequestHandler {

    @Autowired
    BotUserService botUserService;
    @Autowired
    NotificationCache notificationCache;
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    @Lazy
    BotStateContext botStateContext;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {

        long chatId;
        long itemId;

        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            chatId = callbackQuery.getMessage().getChatId();
            // get id of remind item in which user wants to add notification.
            itemId = Long.parseLong(callbackQuery.getData().split(":")[1]);
            // save requested item's id to find it later by chat id.
            notificationCache.put(chatId, itemId);
            return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                    RemindItemState.NOTIFICATION_ADD_TIME);
        } else { // if it's not CallbackQuery, that means user send time of new notification.
            Message message = (Message) botApiObject;
            String userAnswer = message.getText();
            chatId = message.getChatId();
            itemId = notificationCache.take(chatId);
            // find selected previously remind by chat id.
            RemindItem remindItem = botUser.getRemindItems().stream().filter(item -> item.getId() == itemId).findFirst()
                    .get();
            remindItem.addNotification(new RoutineNotification(RoutineNotification.getTimeLongValue(userAnswer)));
            
            botUser.setBotState(BotState.ITEM_EDIT);
            botUserService.save(botUser);
            message.setText(String.valueOf(itemId));
            
            return botStateContext.proccessInputMessage(botUser, message);
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_ADD_NOTIFICATION;
    }

}
