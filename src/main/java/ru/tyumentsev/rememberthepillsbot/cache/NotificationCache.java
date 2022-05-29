package ru.tyumentsev.rememberthepillsbot.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Used to keep current editable item.
 */
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NotificationCache {
    
    // key - chat id, value - id of editable item.
    private Map<Long, Long> remindItems = new HashMap<>();

    public void put(long chatId, long remindItemId) {
        remindItems.put(chatId, remindItemId);
    }

    public Long take(long chatId) {
        if (remindItems.get(chatId) != null) {
            return remindItems.remove(chatId);
        } else {
            return null;
        }
    }
}
