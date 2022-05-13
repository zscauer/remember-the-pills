package ru.tyumentsev.remember_the_pills.handlers.items;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.bot.ReminderBot;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.entity.RemindItem;
import ru.tyumentsev.remember_the_pills.entity.RoutineNotification;
import ru.tyumentsev.remember_the_pills.handlers.UserRequestHandler;
import ru.tyumentsev.remember_the_pills.service.MenuService;
import ru.tyumentsev.remember_the_pills.service.ReplyMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChooseRemindItemHandler implements UserRequestHandler {
    // should return list of active reminders with buttons under each to manage
    // them.
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    MenuService menuService;
    @Autowired
    ApplicationContext applicationContext;

    // TODO: localize messages with localMessageService
    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        // Message message = (Message) botApiObject;
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        // String chatId = String.valueOf(message.getChatId());
        String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
        Set<RemindItem> remindItems = botUser.getRemindItems();
        StringBuilder remindItemDescription = new StringBuilder();

        for (RemindItem remindItem : remindItems) {
            remindItemDescription.delete(0, remindItemDescription.length());
            remindItemDescription.append(remindItem.getName()
                    + " from " + remindItem.getStartDate()
                    + " till " + remindItem.getEndDate()
                    + "\nwith notifications at:\n" + describeRemindItemNotifications(remindItem) + "\n");

            SendMessage sendMessage = menuService.getListOfUserRemindItemsMenuMessage(chatId,
                    remindItemDescription.toString(), remindItem,
                    botUser.getUserLocale());

            ReminderBot reminderBot = applicationContext.getBean("reminderBot", ReminderBot.class);

            try {
                reminderBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return null;
        // return replyMessageService.getReplyMessage(botUser, chatId,
        // "reply.ItemsSuccessfulleLoaded");
    }

    private String describeRemindItemNotifications(RemindItem remindItem) {
        StringBuilder remindItemNotificationsDescription = new StringBuilder();
        for (RoutineNotification notification : remindItem.getNotifications()) {
            remindItemNotificationsDescription.append(notification.getNotificationTime() + "\n");
        }
        return remindItemNotificationsDescription.toString();
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_CHOOSE;
    }

}
