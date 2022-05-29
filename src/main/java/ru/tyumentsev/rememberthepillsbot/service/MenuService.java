package ru.tyumentsev.rememberthepillsbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.entity.RoutineNotification;

/**
 * Provides access to response messages with localized keyboards.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuService {

    LocaleMessageService localeMessageService;
    // keep all required keyboards to get it by user locale and requested menu
    // items.
    Map<UserLocale, Map<String, ReplyKeyboard>> keyboards = new HashMap<>();

    public MenuService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
        putLocalizedKeyboards(UserLocale.ru_RU);
        putLocalizedKeyboards(UserLocale.en_US);
    }

    private void putLocalizedKeyboards(UserLocale userLocale) {
        Map<String, ReplyKeyboard> localizedKeyboards = new HashMap<>();
        localizedKeyboards.put("mainMenuKeyboard", getReplyKeyboard(getMainMenuKeyboard(userLocale)));
        localizedKeyboards.put("helpMenuKeyboard", getReplyKeyboard(getHelpMenuKeyboard(userLocale)));
        localizedKeyboards.put("manageUserProfileKeyboard",
                getUserProfileKeyboard(getUserProfileKeyboardButtons(userLocale)));
        keyboards.put(userLocale, localizedKeyboards);
    }

    public SendMessage getMainMenuMessage(final String chatId, final String textMessage, UserLocale userLocale) {
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, localeMessageService.getMessage(textMessage, userLocale),
                keyboards.get(userLocale).get("mainMenuKeyboard"));

        return mainMenuMessage;
    }

    public SendMessage getHelpMenuMessage(final String chatId, final String textMessage, UserLocale userLocale) {
        final SendMessage helpMenuMessage = createMessageWithKeyboard(chatId, textMessage,
                keyboards.get(userLocale).get("helpMenuKeyboard"));

        return helpMenuMessage;
    }

    public SendMessage getUserProfileMenuMessage(final String chatId, final String textMessage, UserLocale userLocale) {
        final SendMessage userProfileMenuMessage = createMessageWithKeyboard(chatId, textMessage,
                keyboards.get(userLocale).get("manageUserProfileKeyboard"));

        return userProfileMenuMessage;
    }

    public SendMessage getListOfUserRemindItemsMenuMessage(final String chatId, final String textMessage,
            RemindItem remindItem, UserLocale userLocale) {
        final SendMessage userProfileMenuMessage = createMessageWithKeyboard(chatId, textMessage,
                getUserRemindItemKeyboard(remindItem, userLocale));

        return userProfileMenuMessage;
    }

    public SendMessage getListOfUserRoutineNotificationsMenuMessage(final String chatId, final String textMessage,
            RoutineNotification routineNotification,
            UserLocale userLocale) {
        final SendMessage userProfileMenuMessage = createMessageWithKeyboard(chatId, textMessage,
                getUserRoutineNotificationKeyboard(routineNotification, userLocale));

        return userProfileMenuMessage;
    }

    private InlineKeyboardMarkup getUserProfileKeyboard(Map<String, InlineKeyboardButton> userProfileKeyBoard) {

        userProfileKeyBoard.entrySet().stream()
                .forEach(entrySet -> entrySet.getValue().setCallbackData(entrySet.getKey()));

        List<List<InlineKeyboardButton>> allRows = new ArrayList<>();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(userProfileKeyBoard.get("buttonItemsAdd"));
        keyboardButtonsRow1.add(userProfileKeyBoard.get("buttonItemsChoose"));
        allRows.add(keyboardButtonsRow1);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(userProfileKeyBoard.get("buttonItemsDeleteAll"));
        allRows.add(keyboardButtonsRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(allRows);

        return inlineKeyboardMarkup;
    }

    private Map<String, InlineKeyboardButton> getUserProfileKeyboardButtons(UserLocale userLocale) {

        Map<String, InlineKeyboardButton> buttons = new HashMap<>();
        
        buttons.put("buttonItemsAdd", new InlineKeyboardButton(localeMessageService.getMessage("menu.Add", userLocale)));
        buttons.put("buttonItemsChoose", new InlineKeyboardButton(localeMessageService.getMessage("menu.Edit", userLocale)));
        buttons.put("buttonItemsDeleteAll", new InlineKeyboardButton(localeMessageService.getMessage("menu.DeleteAllReminds", userLocale)));

        return buttons;
    }

    private ReplyKeyboardMarkup getReplyKeyboard(List<KeyboardRow> keyboard) {

        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .keyboard(keyboard)
                .build();

        return replyKeyboardMarkup;
    }

    private List<KeyboardRow> getMainMenuKeyboard(UserLocale userLocale) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton(localeMessageService.getMessage("menu.UserReminds", userLocale)));
        row2.add(new KeyboardButton(localeMessageService.getMessage("menu.Help", userLocale)));   

        keyboard.add(row1);
        keyboard.add(row2);

        return keyboard;
    }

    private List<KeyboardRow> getHelpMenuKeyboard(UserLocale userLocale) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        row1.add(new KeyboardButton(localeMessageService.getMessage("menu.MainMenu", userLocale)));
        row2.add(new KeyboardButton(localeMessageService.getMessage("menu.UserReminds", userLocale)));        

        keyboard.add(row1);
        keyboard.add(row2);

        return keyboard;
    }

    /**
     * Generates keyboard for manage selected remind.
     * 
     * @param remindItem remind, that will be managed by this keyboard
     * @param userLocale user locale to define buttons text
     * @return keyboard with buttons, that mapped to current remind
     */
    private ReplyKeyboard getUserRemindItemKeyboard(RemindItem remindItem, UserLocale userLocale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton keyboardButtonAddNotification = new InlineKeyboardButton(localeMessageService.getMessage("menu.AddRemind", userLocale));
        String addRemindNotification = String.format("%s:%s", "buttonItemAddNotification", remindItem.getId());
        keyboardButtonAddNotification.setCallbackData(addRemindNotification);

        InlineKeyboardButton keyboardButtonEdit = new InlineKeyboardButton(localeMessageService.getMessage("menu.Edit", userLocale));
        String editRemind = String.format("%s:%s", "buttonItemEdit", remindItem.getId());
        keyboardButtonEdit.setCallbackData(editRemind);

        InlineKeyboardButton keyboardButtonDelete = new InlineKeyboardButton(localeMessageService.getMessage("menu.Delete", userLocale));
        String deleteRemind = String.format("%s:%s", "buttonItemDelete", remindItem.getId());
        keyboardButtonDelete.setCallbackData(deleteRemind);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(keyboardButtonEdit);
        keyboardButtonsRow1.add(keyboardButtonDelete);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(keyboardButtonAddNotification);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    /**
     * Generates keyboard for manage selected notification.
     * 
     * @param routineNotification notification, that will be managed by this keyboard
     * @param userLocale user locale to define buttons text
     * @return keyboard with buttons, that mapped to current notifications
     */
    private ReplyKeyboard getUserRoutineNotificationKeyboard(RoutineNotification routineNotification,
            UserLocale userLocale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton keyboardButtonEdit = new InlineKeyboardButton(localeMessageService.getMessage("menu.Edit", userLocale));
        String editRemind = String.format("%s:%s", "buttonNotificationEdit", routineNotification.getId());
        keyboardButtonEdit.setCallbackData(editRemind);

        InlineKeyboardButton keyboardButtonDelete = new InlineKeyboardButton(localeMessageService.getMessage("menu.Delete", userLocale));
        String deleteRemind = String.format("%s:%s", "buttonNotificationDelete", routineNotification.getId());
        keyboardButtonDelete.setCallbackData(deleteRemind);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(keyboardButtonEdit);
        keyboardButtonsRow.add(keyboardButtonDelete);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final String chatId, String textMessage,
            final ReplyKeyboard replyKeyboardMarkup) {

        final SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(textMessage)
                .build();
        sendMessage.enableMarkdown(true);

        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
