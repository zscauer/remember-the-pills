package ru.tyumentsev.remember_the_pills.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.bot.UserLocale;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.repository.BotUserRepository;

@Service
public class BotUserService {

    @Autowired
    BotUserRepository botUserRepository;

    public void save(BotUser botUser) {
        if (botUser.getUserLocale() == null) {
            botUser.setUserLocale(UserLocale.en_US);
        }
        botUserRepository.save(botUser);
    }

    public BotUser findByUser(User user) {
        Optional<BotUser> optionalBotUser = botUserRepository.findById(user.getId());
        BotUser foundUser = null;

        if (optionalBotUser.isPresent()) {
            foundUser = optionalBotUser.get();
        } else {
            foundUser = create(user.getId());
        }

        foundUser.setUserLocale(UserLocale.getUserLocaleByCode(user.getLanguageCode()));
        return foundUser;
    }

    public BotUser create(long id) {
        return new BotUser(id);
    }

    public void setBotState(BotUser botUser, String messageText) {
        if (botUser.getBotState() == null) {
            botUser.setBotState(BotState.SHOW_MAIN_MENU);
        }

        if (messageText.contains("buttonItems")) {
            switchItemListMenu(botUser, messageText);
        } else if (messageText.contains("buttonItem")) {
            switchItemMenu(botUser, messageText.split(":")[0]);
        } else if (messageText.contains("buttonNotification")) {
            switchNotificationMenu(botUser, messageText.split(":")[0]);
        } else {
            switchBotMenu(botUser, messageText);
        }
    }

    private void switchItemListMenu(BotUser botUser, String messageText) {
        switch (messageText) {
            case "buttonItemsAdd":
                botUser.setBotState(BotState.ITEM_ADD);
                break;
            case "buttonItemsChoose":
                botUser.setBotState(BotState.ITEM_CHOOSE);
                break;
            case "buttonItemsDeleteAll":
                botUser.setBotState(BotState.ITEM_DELETE_ALL);
                break;
        }
    }

    private void switchItemMenu(BotUser botUser, String messageText) {
        switch (messageText) {
            case "buttonItemEdit":
                botUser.setBotState(BotState.ITEM_EDIT);
                break;
            case "buttonItemDelete":
                botUser.setBotState(BotState.ITEM_DELETE);
                break;
            case "buttonItemAddNotification":
                botUser.setBotState(BotState.ITEM_ADD_NOTIFICATION);
                break;
        }
    }

    private void switchNotificationMenu(BotUser botUser, String messageText) {
        switch (messageText) {
            case "buttonNotificationEdit":
                botUser.setBotState(BotState.ITEM_EDIT_NOTIFICATION);
                break;
            case "buttonNotificationDelete":
                botUser.setBotState(BotState.ITEM_DELETE_NOTIFICATION);
                break;
        }
    }

    private void switchBotMenu(BotUser botUser, String messageText) {
        switch (messageText) {
            case "Main menu":
                botUser.setBotState(BotState.SHOW_MAIN_MENU);
            case "Главное меню":
                botUser.setBotState(BotState.SHOW_MAIN_MENU);
                break;
            case "My notifications":
                botUser.setBotState(BotState.SHOW_USER_INFO);
            case "Мои напоминания":
                botUser.setBotState(BotState.SHOW_USER_INFO);
                break;
            case "Help":
                botUser.setBotState(BotState.SHOW_HELP_MENU);
            case "Помощь":
                botUser.setBotState(BotState.SHOW_HELP_MENU);
                break;
        }
    }
}