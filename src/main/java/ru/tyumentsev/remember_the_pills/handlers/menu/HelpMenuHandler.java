package ru.tyumentsev.remember_the_pills.handlers.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.handlers.UserRequestHandler;
import ru.tyumentsev.remember_the_pills.service.MenuService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HelpMenuHandler implements UserRequestHandler {

    @Autowired
    MenuService menuService;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        Message message = (Message) botApiObject;
        return menuService.getHelpMenuMessage(String.valueOf(message.getChatId()), "This is help page.", botUser.getUserLocale());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }

}
