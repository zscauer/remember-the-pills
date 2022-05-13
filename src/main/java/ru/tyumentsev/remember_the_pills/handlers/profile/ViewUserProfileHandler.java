package ru.tyumentsev.remember_the_pills.handlers.profile;

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
import ru.tyumentsev.remember_the_pills.service.ReplyMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ViewUserProfileHandler implements UserRequestHandler {
    @Autowired
    ReplyMessageService replyMessageService;
    @Autowired
    MenuService menuService;

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_INFO;
    }

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        Message message = (Message) botApiObject;
        return menuService.getUserProfileMenuMessage(String.valueOf(message.getChatId()),
                "List of reminders (from " + this.getClass() + "):\n" + botUser.getListOfRemindItems(),
                botUser.getUserLocale());
    }

}
