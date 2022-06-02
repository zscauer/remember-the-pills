package ru.tyumentsev.rememberthepillsbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocaleMessageServiceTest {

    @Mock
    MessageSource messageSource;
    LocaleMessageService localeMessageService;
    
    static final String TEXT_MESSAGE = "Test message";
    static final String SUCCESS_TEST_MESSAGE = "Success";

    @BeforeEach
    void init() {
        when(messageSource.getMessage(TEXT_MESSAGE, null, Locale.forLanguageTag(UserLocale.en_US.toString())))
                .thenReturn(SUCCESS_TEST_MESSAGE);
        localeMessageService = new LocaleMessageService(messageSource);
    }

    @Test
    void testGetMessage() {
        assertEquals(SUCCESS_TEST_MESSAGE, localeMessageService.getMessage(TEXT_MESSAGE, UserLocale.en_US));
        verify(messageSource).getMessage(TEXT_MESSAGE, null, Locale.forLanguageTag(UserLocale.en_US.toString()));
    }
}
