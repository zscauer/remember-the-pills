package ru.tyumentsev.rememberthepillsbot.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.repository.RemindItemRepository;

/**
 * Provides access to repository of reminds.
 * 
 * @see RemindItem
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RemindItemService {
    
    RemindItemRepository remindItemRepository;

    public RemindItem create(BotUser botUser) {
        return new RemindItem(botUser);
    }

    public RemindItem create(RemindItemCacheEntity remindItemCacheEntity, BotUser botUser) {
        RemindItem remindItem = new RemindItem(botUser);
        remindItem.setName(remindItemCacheEntity.getName());
        remindItem.setStartDate(remindItemCacheEntity.getStartDate());
        remindItem.setEndDate(remindItemCacheEntity.getEndDate());
        
        return remindItem;
    }

    public RemindItem saveItem(RemindItem remindItem) {
        return remindItemRepository.saveAndFlush(remindItem);
    }

    public void deleteItem(RemindItem remindItem) {
        remindItemRepository.delete(remindItem);
    }

    public RemindItem getById(long id) {
        return remindItemRepository.getById(id);
    }


}
