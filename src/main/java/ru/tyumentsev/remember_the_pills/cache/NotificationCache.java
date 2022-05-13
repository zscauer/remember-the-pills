package ru.tyumentsev.remember_the_pills.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class NotificationCache {
    
    private Map<Long, Long> remindItems = new HashMap<>();

    public void put(long chatId, long remindItemId) {
        remindItems.put(chatId, remindItemId);
    }

    public long take(long chatId) {
        return remindItems.remove(chatId);
    }
}
