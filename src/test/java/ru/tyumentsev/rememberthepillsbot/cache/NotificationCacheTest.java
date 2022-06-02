package ru.tyumentsev.rememberthepillsbot.cache;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCacheTest {
    static final long CHAT_ID = 111_111L;
    static final long ITEM_ID = 111_111L;

    static NotificationCache notificationCache;

    @BeforeAll
    static void init() {
        notificationCache = new NotificationCache();
    }

    @Test
    void testPut() {
        assertDoesNotThrow(() -> {
            notificationCache.put(CHAT_ID, ITEM_ID);
        });

    }

    @Test
    void testTake() {
        assertEquals(ITEM_ID, notificationCache.take(CHAT_ID));
        assertNull(notificationCache.take(CHAT_ID));
    }
}
