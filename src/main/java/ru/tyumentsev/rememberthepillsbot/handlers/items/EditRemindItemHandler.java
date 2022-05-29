package ru.tyumentsev.rememberthepillsbot.handlers.items;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.ReminderBot;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.MenuService;

/**
 * Handle user request of notification's list to edit notification time.
 * 
 * @see UserRequestHandler
 * @see RemindItem
 * @see RoutineNotification
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditRemindItemHandler implements UserRequestHandler {

    @Autowired
    MenuService menuService;
    @Autowired
    @Lazy
    ReminderBot reminderBot;

    // return list of notifications of reminder item with buttons under each to
    // manage notification.
    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        String chatId;
        long itemId;
        // ReminderBot reminderBot = applicationContext.getBean("reminderBot", ReminderBot.class);

        // get id of item, which notifications user wants to edit.
        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            chatId = String.valueOf(callbackQuery.getMessage().getChatId());
            itemId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        } else {
            Message message = (Message) botApiObject;
            chatId = String.valueOf(message.getChatId());
            itemId = Long.parseLong(message.getText());
        }

        RemindItem remindItem = botUser.getRemindItems().stream()
                .filter(item -> item.getId() == itemId).findFirst().get();
        Set<RoutineNotification> routineNotifications = remindItem.getNotifications();

        StringBuilder routineNotificationDescription = new StringBuilder();

        for (RoutineNotification routineNotification : routineNotifications) {
            routineNotificationDescription.delete(0, routineNotificationDescription.length());

            routineNotificationDescription
                    .append(remindItem.getName() + ": " + routineNotification.getNotificationTime() + "\n");

            SendMessage sendMessage = menuService.getListOfUserRoutineNotificationsMenuMessage(chatId,
                    routineNotificationDescription.toString(), routineNotification,
                    botUser.getUserLocale());

            try {
                reminderBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_EDIT;
    }

}
