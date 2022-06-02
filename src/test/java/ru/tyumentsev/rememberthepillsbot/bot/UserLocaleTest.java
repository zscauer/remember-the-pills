package ru.tyumentsev.rememberthepillsbot.bot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLocaleTest {
    static Map<String, UserLocale> locales = new HashMap<>();

    @BeforeAll
    static void init() {
        locales.put("Ru", UserLocale.ru_RU);
        locales.put("en", UserLocale.en_US);
        locales.put("fr", UserLocale.en_US);
        locales.put(null, UserLocale.en_US);
    }

    @Test
    void testGetUserLocaleByCode() {
        locales.forEach((k, v) -> assertEquals(v, UserLocale.getUserLocaleByCode(k)));
    }
}
