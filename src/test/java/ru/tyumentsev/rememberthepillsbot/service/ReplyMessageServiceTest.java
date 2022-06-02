package ru.tyumentsev.rememberthepillsbot.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReplyMessageServiceTest {

    final static String TEXT_MESSAGE = "reply.showUserProfile";
    final static String CHAT_ID = "111222333";
    final static String RETURN_TEXT_MESSAGE = "Text of user profile.";
    final static UserLocale LOCALE_EN = UserLocale.en_US;

    @Mock
    LocaleMessageService localeMessageService;
    @Mock
    MenuService menuService;
    ReplyMessageService replyMessageService;

    @BeforeEach
    void init() {
        replyMessageService = new ReplyMessageService(localeMessageService, menuService);
    }

    @Test
    void testDefaultStateMessagesFilling() {
        Field stateMessagesField;
        try {
            stateMessagesField = replyMessageService.getClass().getDeclaredField("stateMessages");
            stateMessagesField.setAccessible(true);
            Map<?, ?> stateMessage = (HashMap<?, ?>) stateMessagesField.get(replyMessageService);
            assertFalse(stateMessage.isEmpty());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testGetReplyMessage() {
        when(localeMessageService.getMessage(anyString(), any(UserLocale.class))).thenReturn(RETURN_TEXT_MESSAGE);
        when(menuService.getUserProfileMenuMessage(eq(CHAT_ID), any(), any(UserLocale.class)))
                .thenReturn(new SendMessage(CHAT_ID, RETURN_TEXT_MESSAGE));

        BotUser botUser = getTestBotUser();

        replyMessageService.getReplyMessage(botUser, CHAT_ID, RemindItemState.ITEM_SUCCESSFULLY_ADDED);
        verify(menuService, atLeastOnce()).getUserProfileMenuMessage(eq(CHAT_ID), any(), any(UserLocale.class));

        replyMessageService.getReplyMessage(botUser, CHAT_ID, RemindItemState.ITEM_SET_NAME);
        verify(localeMessageService, atLeastOnce()).getMessage(anyString(), any());
    }

    @Test
    void testGetReplyMessage2() {
        when(localeMessageService.getMessage(anyString(), any(UserLocale.class))).thenReturn(RETURN_TEXT_MESSAGE);

        BotUser botUser = getTestBotUser();

        replyMessageService.getReplyMessage(botUser, CHAT_ID, TEXT_MESSAGE);
        verify(localeMessageService).getMessage(anyString(), any());
    }

    BotUser getTestBotUser() {
        BotUser botUser = new BotUser();
        botUser.setUserLocale(LOCALE_EN);
        botUser.setRemindItems(new HashSet<>());

        return botUser;
    }
}
