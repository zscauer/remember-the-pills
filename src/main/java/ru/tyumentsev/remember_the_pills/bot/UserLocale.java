package ru.tyumentsev.remember_the_pills.bot;

public enum UserLocale {
    ru_RU,
    en_US;

    public static UserLocale getUserLocaleByCode(String localeCode) {
        switch (localeCode) {
            case "ru":
                return ru_RU;
            default:
                return en_US;
        }
    }
}
