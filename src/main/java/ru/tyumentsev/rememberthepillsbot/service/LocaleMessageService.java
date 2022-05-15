package ru.tyumentsev.rememberthepillsbot.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;

/**
 * Get localized text of message based on user language.
 * 
 * @see UserLocale
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LocaleMessageService {
    
    @Autowired
    MessageSource messageSource;

    public String getMessage(String message, UserLocale userLocale) {
        return messageSource.getMessage(message, null, Locale.forLanguageTag(userLocale.toString()));
    }

    public String getMessage(String message, UserLocale userLocale, Object... args) {
        return messageSource.getMessage(message, args, Locale.forLanguageTag(userLocale.toString()));
    }

}
