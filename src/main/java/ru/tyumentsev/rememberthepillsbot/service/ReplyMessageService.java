package ru.tyumentsev.rememberthepillsbot.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;
import ru.tyumentsev.rememberthepillsbot.bot.States;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

/**
 * Generates reply messages defined by user state and locale.
 * 
 * @see LocaleMessageService
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReplyMessageService {

    LocaleMessageService localeMessageService;
    MenuService menuService;

    Map<States, String> stateMessages = fillDefaultStateMessages();

    private Map<States, String> fillDefaultStateMessages() {
        Map<States, String> messages = new HashMap<>();
        
        messages.put(RemindItemState.ITEM_SET_NAME, "reply.AskItemName");
        messages.put(RemindItemState.ITEM_SET_START_DATE, "reply.AskItemStartDate");
        messages.put(RemindItemState.ITEM_SET_END_DATE, "reply.AskItemEndDate");
        messages.put(RemindItemState.NOTIFICATION_ADD_TIME, "reply.AskNotificationTime");
        messages.put(RemindItemState.ERROR_INCORRECT_DATE_FORMAT, "reply.IncorrectDateFormatError");
        messages.put(RemindItemState.ITEM_SUCCESSFULLY_ADDED, "reply.ItemSuccessfullyAdded");

        return messages;
    }

    public SendMessage getReplyMessage(final BotUser botUser, final String chatId, final States itemState) {
        UserLocale userLocale = botUser.getUserLocale();

        if (itemState == RemindItemState.ITEM_SUCCESSFULLY_ADDED) {
            return menuService.getUserProfileMenuMessage(chatId,
                    localeMessageService.getMessage("reply.showUserProfile", userLocale)
                            + ":\n" + botUser.getListOfRemindItems(),
                    botUser.getUserLocale());
        } else {
            return new SendMessage(chatId,
                    localeMessageService.getMessage(stateMessages.get(itemState), botUser.getUserLocale()));
        }
    }

    public SendMessage getReplyMessage(final BotUser botUser, final String chatId, final String messageText) {
        return new SendMessage(chatId, localeMessageService.getMessage(messageText, botUser.getUserLocale()));
    }

    public SendMessage getReplyMessage(final BotUser botUser, final String chatId, final States itemState, Object... args) {
        return new SendMessage(chatId,
                localeMessageService.getMessage(stateMessages.get(itemState), botUser.getUserLocale(), args));
    }
}
