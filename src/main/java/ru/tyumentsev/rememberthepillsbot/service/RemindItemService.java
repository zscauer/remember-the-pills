package ru.tyumentsev.rememberthepillsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class RemindItemService {
    
    @Autowired
    RemindItemRepository remindItemRepository;

    public RemindItem create(BotUser botUser) {
        return new RemindItem(botUser);
    }

    public RemindItem create(RemindItemCacheEntity remindItemCacheEntity, BotUser botUser) {
        RemindItem remindItem = new RemindItem();
        remindItem.setBotUser(botUser);
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

    public RemindItem findById(long id) {
        return remindItemRepository.getById(id);
    }


}
