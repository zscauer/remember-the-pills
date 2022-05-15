package ru.tyumentsev.rememberthepillsbot.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Used to keep current editable item.
 */
@Component
public class NotificationCache {
    
    // key - chat id, value - id of editable item.
    private Map<Long, Long> remindItems = new HashMap<>();

    public void put(long chatId, long remindItemId) {
        remindItems.put(chatId, remindItemId);
    }

    public long take(long chatId) {
        return remindItems.remove(chatId);
    }
}
