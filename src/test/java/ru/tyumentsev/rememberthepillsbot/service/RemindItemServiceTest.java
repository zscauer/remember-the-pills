package ru.tyumentsev.rememberthepillsbot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.repository.RemindItemRepository;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemindItemServiceTest {
    
    static BotUser botUser;
    static long USER_ID = 333L;
    static long ITEM_ID = 666L;

    @Mock
    RemindItemRepository remindItemRepository;
    RemindItemService remindItemService;
    RemindItem remindItem;

    @BeforeAll
    static void initAll() {
        botUser = new BotUser(USER_ID);
    }
    
    @BeforeEach
    void init() {
        remindItemService = new RemindItemService(remindItemRepository);
        remindItem = new RemindItem(botUser);
        remindItem.setId(ITEM_ID);
    }

    @Test
    void testCreate() {
        assertEquals(botUser, remindItemService.create(botUser).getBotUser());
        assertEquals(botUser.getId(), remindItemService.create(botUser).getBotUser().getId());
    }

    @Test
    void testCreate2() {
        final String USER_NAME = "Chuck Norris";
        final Date START_DATE = new Date(1641027390);
        final Date END_DATE = new Date(1643705790);

        RemindItemCacheEntity remindItemCacheEntity = new RemindItemCacheEntity();
        remindItemCacheEntity.setName(USER_NAME);
        remindItemCacheEntity.setStartDate(START_DATE);
        remindItemCacheEntity.setEndDate(END_DATE);

        RemindItem newRemindItem = remindItemService.create(remindItemCacheEntity, botUser);

        assertEquals(newRemindItem.getBotUser().getId(), botUser.getId());
        assertEquals(newRemindItem.getName(), remindItemCacheEntity.getName());
        assertEquals(newRemindItem.getStartDate(), remindItemCacheEntity.getStartDate());
        assertEquals(newRemindItem.getEndDate(), remindItemCacheEntity.getEndDate());
    }

    @Test
    void testDeleteItem() {
        remindItemService.deleteItem(remindItem);
        verify(remindItemRepository).delete(remindItem);
    }

    @Test
    void testFindById() {
        when(remindItemRepository.getById(ITEM_ID)).thenReturn(remindItem);
        
        assertEquals(remindItem, remindItemService.getById(ITEM_ID));
        verify(remindItemRepository).getById(ITEM_ID);
    }

    @Test
    void testSaveItem() {
        when(remindItemRepository.saveAndFlush(remindItem)).thenReturn(remindItem);

        assertEquals(remindItem, remindItemService.saveItem(remindItem));
        verify(remindItemRepository).saveAndFlush(remindItem);
    }
}
