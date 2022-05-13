package ru.tyumentsev.remember_the_pills.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.RemindItemState;
import ru.tyumentsev.remember_the_pills.bot.States;
import ru.tyumentsev.remember_the_pills.entity.BotUser;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyMessageService {
    @Autowired
    LocaleMessageService localeMessageService;
    @Autowired
    MenuService menuService;

    Map<States, String> stateMessages = new HashMap<>();

    public ReplyMessageService() {
        stateMessages.put(RemindItemState.ITEM_SET_NAME, "reply.AskItemName");
        stateMessages.put(RemindItemState.ITEM_SET_START_DATE, "reply.AskItemStartDate");
        stateMessages.put(RemindItemState.ITEM_SET_END_DATE, "reply.AskItemEndDate");
        stateMessages.put(RemindItemState.NOTIFICATION_ADD_TIME, "reply.AskNotificationTime");
        stateMessages.put(RemindItemState.ERROR_INCORRECT_DATE_FORMAT, "reply.IncorrectDateFormatError");
        stateMessages.put(RemindItemState.ITEM_SUCCESSFULLY_ADDED, "reply.ItemSuccessfullyAdded");
    }

    public SendMessage getReplyMessage(BotUser botUser, String chatId, States itemState) {
        if (itemState == RemindItemState.ITEM_SUCCESSFULLY_ADDED) {
            return menuService.getUserProfileMenuMessage(chatId,
                    "List of reminders (from " + this.getClass() + "):\n" + botUser.getListOfRemindItems(),
                    botUser.getUserLocale());
        } else {
            return new SendMessage(chatId,
                    localeMessageService.getMessage(stateMessages.get(itemState), botUser.getUserLocale()));
        }
    }

    public SendMessage getReplyMessage(BotUser botUser, String chatId, String messageText) {
        return new SendMessage(chatId, localeMessageService.getMessage(messageText, botUser.getUserLocale()));
    }

    public SendMessage getReplyMessage(BotUser botUser, String chatId, States itemState, Object... args) {
        return new SendMessage(chatId,
                localeMessageService.getMessage(stateMessages.get(itemState), botUser.getUserLocale(), args));
    }
}
