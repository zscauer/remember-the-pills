package ru.tyumentsev.remember_the_pills.service;

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
import ru.tyumentsev.remember_the_pills.bot.RemindItemState;
import ru.tyumentsev.remember_the_pills.bot.UserLocale;
import ru.tyumentsev.remember_the_pills.entity.RemindItem;
import ru.tyumentsev.remember_the_pills.entity.RoutineNotification;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuService {
    Map<UserLocale, Map<String, ReplyKeyboard>> keyboards = new HashMap<>();

    public MenuService() {
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
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, textMessage,
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
            RemindItem remindItem,
            UserLocale userLocale) {
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
        // keyboardButtonsRow2.add(userProfileKeyBoard.get("buttonDeleteItem"));
        keyboardButtonsRow2.add(userProfileKeyBoard.get("buttonItemsDeleteAll"));
        allRows.add(keyboardButtonsRow2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(allRows);

        return inlineKeyboardMarkup;
    }

    private Map<String, InlineKeyboardButton> getUserProfileKeyboardButtons(UserLocale userLocale) {

        Map<String, InlineKeyboardButton> buttons = new HashMap<>();

        switch (userLocale) {
            case ru_RU:
                buttons.put("buttonItemsAdd", new InlineKeyboardButton("Добавить"));
                buttons.put("buttonItemsChoose", new InlineKeyboardButton("Редактировать"));
                // buttons.put("buttonDeleteItem", new InlineKeyboardButton("Удалить
                // напоминание"));
                buttons.put("buttonItemsDeleteAll", new InlineKeyboardButton("Удалить все напоминания"));
                break;
            default:
                buttons.put("buttonItemsAdd", new InlineKeyboardButton("Add"));
                buttons.put("buttonItemsChoose", new InlineKeyboardButton("Edit"));
                // buttons.put("buttonDeleteItem", new InlineKeyboardButton("Delete reminder"));
                buttons.put("buttonItemsDeleteAll", new InlineKeyboardButton("Delete all reminders"));
                break;
        }

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

        switch (userLocale) {
            case ru_RU:
                row1.add(new KeyboardButton("Мои напоминания"));
                row2.add(new KeyboardButton("Помощь"));
                break;

            default:
                row1.add(new KeyboardButton("My reminders"));
                row2.add(new KeyboardButton("Help"));
                break;
        }

        keyboard.add(row1);
        keyboard.add(row2);

        return keyboard;
    }

    private List<KeyboardRow> getHelpMenuKeyboard(UserLocale userLocale) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        switch (userLocale) {
            case ru_RU:
                row1.add(new KeyboardButton("Главное меню"));
                row2.add(new KeyboardButton("Мои напоминания"));
                break;
            default:
                row1.add(new KeyboardButton("Main menu"));
                row2.add(new KeyboardButton("My reminders"));
                break;
        }

        keyboard.add(row1);
        keyboard.add(row2);

        return keyboard;
    }

    private ReplyKeyboard getUserRemindItemKeyboard(RemindItem remindItem, UserLocale userLocale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton keyboardButtonAddNotification = new InlineKeyboardButton("Добавить уведомление");
        String addRemindNotification = String.format("%s:%s", "buttonItemAddNotification", remindItem.getId());
        keyboardButtonAddNotification.setCallbackData(addRemindNotification);
        
        InlineKeyboardButton keyboardButtonEdit = new InlineKeyboardButton("Изменить");
        String editRemind = String.format("%s:%s", "buttonItemEdit", remindItem.getId());
        keyboardButtonEdit.setCallbackData(editRemind);

        InlineKeyboardButton keyboardButtonDelete = new InlineKeyboardButton("Удалить");
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

    private ReplyKeyboard getUserRoutineNotificationKeyboard(RoutineNotification routineNotification, UserLocale userLocale) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton keyboardButtonEdit = new InlineKeyboardButton("Изменить");
        String editRemind = String.format("%s:%s", "buttonNotificationEdit", routineNotification.getId());
        keyboardButtonEdit.setCallbackData(editRemind);

        InlineKeyboardButton keyboardButtonDelete = new InlineKeyboardButton("Удалить");
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
