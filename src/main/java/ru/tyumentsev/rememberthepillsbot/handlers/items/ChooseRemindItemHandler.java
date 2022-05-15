package ru.tyumentsev.rememberthepillsbot.handlers.items;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.ReminderBot;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.LocaleMessageService;
import ru.tyumentsev.rememberthepillsbot.service.MenuService;

/**
 * Handle request of list of user reminders.
 * 
 * @see UserRequestHandler
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChooseRemindItemHandler implements UserRequestHandler {

    @Autowired
    MenuService menuService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    LocaleMessageService localeMessageService;

    // return list of user reminders with buttons under each to manage them.
    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
        UserLocale userLocale = botUser.getUserLocale();
        String chatId = String.valueOf(callbackQuery.getMessage().getChatId());
        Set<RemindItem> remindItems = botUser.getRemindItems();
        StringBuilder remindItemDescription = new StringBuilder();

        ReminderBot reminderBot = applicationContext.getBean("reminderBot", ReminderBot.class);

        for (RemindItem remindItem : remindItems) {
            remindItemDescription.delete(0, remindItemDescription.length());
            remindItemDescription.append(remindItem.getName()
                    + " " + localeMessageService.getMessage("keyWords.From", userLocale) + " "
                    + LocalDate.ofInstant(remindItem.getStartDate().toInstant(), ZoneId.systemDefault())
                    + " " + localeMessageService.getMessage("keyWords.Till", userLocale) + " "
                    + LocalDate.ofInstant(remindItem.getEndDate().toInstant(), ZoneId.systemDefault())
                    + "\n" + localeMessageService.getMessage("keyWords.WithNotificationsAt", userLocale) + "\n"
                    + describeRemindItemNotifications(remindItem) + "\n");

            SendMessage sendMessage = menuService.getListOfUserRemindItemsMenuMessage(chatId,
                    remindItemDescription.toString(), remindItem,
                    botUser.getUserLocale());

            try {
                reminderBot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return null;
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
