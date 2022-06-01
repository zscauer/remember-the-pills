package ru.tyumentsev.rememberthepillsbot.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.repository.BotUserRepository;

/**
 * Provides access to repository of user of bot
 * and define users states by their requests.
 * 
 * @see BotUser
 * @see BotState
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BotUserService {

    BotUserRepository botUserRepository;
    Map<String, BotState> menuStates = generateMenuStates();
    
    public BotUser create(final long id) {
        return new BotUser(id);
    }
    
    public void save(BotUser botUser) {
        if (botUser.getUserLocale() == null) {
            botUser.setUserLocale(UserLocale.en_US);
        }
        
        botUserRepository.save(botUser);
    }

    public BotUser findByUser(User user) {
        Optional<BotUser> optionalBotUser = botUserRepository.findById(user.getId());
        BotUser foundUser = optionalBotUser.isPresent() ? optionalBotUser.get() : create(user.getId());

        foundUser.setUserLocale(UserLocale.getUserLocaleByCode(user.getLanguageCode()));
        
        return foundUser;
    }

    public void setBotState(BotUser botUser, String messageText) {
        if (messageText.contains("buttonItems")) {
            botUser.setBotState(menuStates.getOrDefault(messageText, BotState.SHOW_MAIN_MENU));    
        } else {
            botUser.setBotState(menuStates.getOrDefault(messageText.split(":")[0], BotState.SHOW_MAIN_MENU));
        }
    }

    private Map<String, BotState> generateMenuStates() {
        Map<String, BotState> states = new HashMap<>();
        
        states.put("buttonItemsAdd", BotState.ITEM_ADD);
        states.put("buttonItemsChoose", BotState.ITEM_CHOOSE);
        states.put("buttonItemsDeleteAll", BotState.ITEM_DELETE_ALL);
        states.put("buttonItemEdit", BotState.ITEM_EDIT);
        states.put("buttonItemDelete", BotState.ITEM_DELETE);
        states.put("buttonItemAddNotification", BotState.ITEM_ADD_NOTIFICATION);
        states.put("buttonNotificationEdit", BotState.ITEM_EDIT_NOTIFICATION);
        states.put("buttonNotificationDelete", BotState.ITEM_DELETE_NOTIFICATION);

        states.put("Main menu", BotState.SHOW_MAIN_MENU);
        states.put("Главное меню", BotState.SHOW_MAIN_MENU);
        states.put("My reminders", BotState.SHOW_USER_INFO);
        states.put("Мои напоминания", BotState.SHOW_USER_INFO);
        states.put("Help", BotState.SHOW_HELP_MENU);
        states.put("Помощь", BotState.SHOW_HELP_MENU);
        
        return states;
    }
}