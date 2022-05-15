package ru.tyumentsev.rememberthepillsbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.repository.RemindItemCacheRepository;

/**
 * Provides access to repository of reminds cache.
 * 
 * @see RemindItemCacheEntity
 */
@Service
public class RemindItemCacheService {
    
    @Autowired
    RemindItemCacheRepository remindItemMongoRepository;

    public RemindItemCacheEntity findByChatId(long chatId) {
        return remindItemMongoRepository.findByChatId(chatId);
    }

    public void save(RemindItemCacheEntity remindItemCacheEntity) {
        remindItemMongoRepository.save(remindItemCacheEntity);
    }

    public void delete(RemindItemCacheEntity remindItemCacheEntity) {
        remindItemMongoRepository.delete(remindItemCacheEntity);
    }


}
