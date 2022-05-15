package ru.tyumentsev.rememberthepillsbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;

/**
 * Provides access to saved in cache entities of reminds.
 * 
 * @see RemindItemCacheEntity
 */
@Repository
public interface RemindItemCacheRepository extends MongoRepository<RemindItemCacheEntity, String> {
    RemindItemCacheEntity findByChatId(long chatId);
}
