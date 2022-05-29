package ru.tyumentsev.rememberthepillsbot.handlers.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.MenuService;

/**
 * Handle user request of main page.
 * 
 * @see UserRequestHandler
 * @see MenuService
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MainMenuHandler implements UserRequestHandler {

    MenuService menuService;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        Message message = (Message) botApiObject;
        return menuService.getMainMenuMessage(String.valueOf(message.getChatId()),
                "menu.MainMenuGreeting", botUser.getUserLocale());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;

    }

}
