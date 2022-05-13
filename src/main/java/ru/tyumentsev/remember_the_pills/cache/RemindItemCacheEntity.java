package ru.tyumentsev.remember_the_pills.cache;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.RemindItemState;

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
