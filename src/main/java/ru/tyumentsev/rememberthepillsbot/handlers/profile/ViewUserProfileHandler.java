package ru.tyumentsev.rememberthepillsbot.handlers.profile;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.tyumentsev.rememberthepillsbot.service.LocaleMessageService;
import ru.tyumentsev.rememberthepillsbot.service.MenuService;

/**
 * Handle user request of profile with reminds.
 * 
 * @see UserRequestHandler
 * @see MenuService
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ViewUserProfileHandler implements UserRequestHandler {

    @Autowired
    MenuService menuService;
    @Autowired
    LocaleMessageService localeMessageService;

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        Message message = (Message) botApiObject;
        return menuService.getUserProfileMenuMessage(String.valueOf(message.getChatId()),
                localeMessageService.getMessage("reply.showUserProfile", botUser.getUserLocale()) + "\n"
                        + botUser.getListOfRemindItems(),
                botUser.getUserLocale());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_INFO;
    }

}
