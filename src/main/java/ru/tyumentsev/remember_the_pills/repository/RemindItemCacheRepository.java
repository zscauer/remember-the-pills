package ru.tyumentsev.remember_the_pills.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.tyumentsev.remember_the_pills.cache.RemindItemCacheEntity;

@Repository
public interface RemindItemCacheRepository extends MongoRepository<RemindItemCacheEntity, String> {
    RemindItemCacheEntity findByChatId(long chatId);
}
