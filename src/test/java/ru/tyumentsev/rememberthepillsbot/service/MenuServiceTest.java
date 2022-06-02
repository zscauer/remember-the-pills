package ru.tyumentsev.rememberthepillsbot.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuServiceTest {

    @Mock
    LocaleMessageService localeMessageService;
    MenuService menuService;

    @Test
    void testKeyboardsFilling() {
        when(localeMessageService.getMessage(anyString(), any(UserLocale.class)))
                .thenAnswer((invocationOnMock) -> invocationOnMock.getArgument(0));

        Set<UserLocale> userLocales = Arrays.stream(UserLocale.values()).collect(Collectors.toSet());
        menuService = new MenuService(localeMessageService, userLocales);

        Field keyboardsField;
        try {
            keyboardsField = menuService.getClass().getDeclaredField("keyboards");
            keyboardsField.setAccessible(true);
            Map<?, ?> keyboards = (HashMap<?, ?>) keyboardsField.get(menuService);
            assertFalse(keyboards.isEmpty());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
