package ru.tyumentsev.rememberthepillsbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.repository.BotUserRepository;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotUserServiceTest {

    @Mock
    BotUser botUser;
    @Mock
    BotUserRepository botUserRepository;
    BotUserService botUserService;

    final static long USER_ID = 333L;

    @BeforeEach
    void init() {
        botUserService = new BotUserService(botUserRepository);
        botUser.setId(USER_ID);
    }

    @Test
    void testCreate() {
        BotUser newUser = botUserService.create(USER_ID);
        assertEquals(newUser.getId(), USER_ID);
        assertNotEquals(newUser.getId(), USER_ID * 2);
    }

    @Test
    void testSave() {
        botUserService.save(botUser);
        verify(botUserRepository).save(botUser);
        verify(botUser).setUserLocale(UserLocale.en_US);
    }

    @Test
    void testFindByUser() {
        when(botUserRepository.findById(USER_ID))
                .thenReturn(Optional.of(botUser));

        assertEquals(botUser, botUserService.findByUser(new User(USER_ID, "testUser", false)));
        verify(botUser).setUserLocale(any());
    }

    @Test
    void testSetBotState() {
        botUserService.setBotState(botUser, "buttonItemsText");
        verify(botUser).setBotState(any());
    }
}
