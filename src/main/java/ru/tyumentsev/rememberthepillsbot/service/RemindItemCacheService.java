package ru.tyumentsev.rememberthepillsbot.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.repository.RemindItemCacheRepository;

/**
 * Provides access to repository of reminds cache.
 * 
 * @see RemindItemCacheEntity
 */
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RemindItemCacheService {
    
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
