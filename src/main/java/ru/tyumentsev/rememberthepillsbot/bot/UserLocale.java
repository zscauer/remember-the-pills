package ru.tyumentsev.rememberthepillsbot.bot;

/**
 * Used to define text of output message
 * based on user's application language.
 * 
 * @see ReplyMessageService
 */
public enum UserLocale {
    ru_RU,
    en_US;

    public static UserLocale getUserLocaleByCode(final String localeCode) {
        if (localeCode == null) {
            return en_US;
        } else if (localeCode.equalsIgnoreCase("ru")) {
            return ru_RU;
        } else {
            return en_US;
        }
    }
}
