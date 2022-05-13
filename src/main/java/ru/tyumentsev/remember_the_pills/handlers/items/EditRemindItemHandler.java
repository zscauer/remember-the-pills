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
import ru.tyumentsev.remember_the_pills.service.RemindItemService;
import ru.tyumentsev.remember_the_pills.service.ReplyMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EditRemindItemHandler implements UserRequestHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    MenuService menuService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    RemindItemService remindItemService;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        String chatId;
        long itemId;
        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            chatId = String.valueOf(callbackQuery.getMessage().getChatId());
            itemId = Long.parseLong(callbackQuery.getData().split(":")[1]);
        } else {
            Message message = (Message) botApiObject;
            chatId = String.valueOf(message.getChatId());
            itemId = Long.parseLong(message.getText());
        }
        
        RemindItem remindItem = botUser.getRemindItems().stream().filter(item -> item.getId() == itemId).findFirst().get();
        Set<RoutineNotification> routineNotifications = remindItem.getNotifications();

        StringBuilder routineNotificationDescription = new StringBuilder();

        for (RoutineNotification routineNotification : routineNotifications) {
            routineNotificationDescription.delete(0, routineNotificationDescription.length());

            routineNotificationDescription
                    .append(remindItem.getName() + ": " + routineNotification.getNotificationTime() + "\n");

            SendMessage sendMessage = menuService.getListOfUserRoutineNotificationsMenuMessage(chatId,
                    routineNotificationDescription.toString(), routineNotification,
                    botUser.getUserLocale());

            ReminderBot reminderBot = applicationContext.getBean("reminderBot",
                    ReminderBot.class);

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
