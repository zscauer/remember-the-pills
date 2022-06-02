package ru.tyumentsev.rememberthepillsbot.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.repository.RemindItemCacheRepository;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemindItemCacheServiceTest {

    @Mock
    RemindItemCacheRepository remindItemMongoRepository;
    RemindItemCacheService remindItemCacheService;
    RemindItemCacheEntity remindItemCacheEntity;

    static final long CHAT_ID = 333L;

    @BeforeEach
    void init() {
        remindItemCacheService = new RemindItemCacheService(remindItemMongoRepository);
        remindItemCacheEntity = new RemindItemCacheEntity();
        remindItemCacheEntity.setChatId(CHAT_ID);
    }

    @Test
    void testDelete() {
        remindItemCacheService.delete(remindItemCacheEntity);
        verify(remindItemMongoRepository).delete(remindItemCacheEntity);
    }

    @Test
    void testFindByChatId() {
        when(remindItemMongoRepository.findByChatId(CHAT_ID)).thenReturn(remindItemCacheEntity);
        
        remindItemCacheService.findByChatId(remindItemCacheEntity.getChatId());
        verify(remindItemMongoRepository).findByChatId(CHAT_ID);
    }

    @Test
    void testSave() {
        remindItemCacheService.save(remindItemCacheEntity);
        verify(remindItemMongoRepository).save(remindItemCacheEntity);
    }
}
