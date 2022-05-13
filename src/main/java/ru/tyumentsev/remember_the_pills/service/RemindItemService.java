package ru.tyumentsev.remember_the_pills.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.tyumentsev.remember_the_pills.cache.RemindItemCacheEntity;
import ru.tyumentsev.remember_the_pills.entity.BotUser;
import ru.tyumentsev.remember_the_pills.entity.RemindItem;
import ru.tyumentsev.remember_the_pills.repository.RemindItemRepository;

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
