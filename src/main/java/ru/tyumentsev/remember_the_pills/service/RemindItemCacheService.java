package ru.tyumentsev.remember_the_pills.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.tyumentsev.remember_the_pills.cache.RemindItemCacheEntity;
import ru.tyumentsev.remember_the_pills.repository.RemindItemCacheRepository;

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
