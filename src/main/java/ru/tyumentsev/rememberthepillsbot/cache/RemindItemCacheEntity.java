package ru.tyumentsev.rememberthepillsbot.cache;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;

/**
 * Used as a cache till all fields of cache entity will be filled.
 * 
 * @see RemindItem
 */
@Data
@Document(collection = "remindItemData")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemindItemCacheEntity {
    @Id
    String id;
    String name;
    Date startDate;
    Date endDate;
    long chatId;
    RemindItemState remindItemState;
}
