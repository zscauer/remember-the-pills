// package ru.tyumentsev.remember_the_pills.cache;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.stereotype.Component;

// import ru.tyumentsev.remember_the_pills.entity.RemindItem;

// @Component
// public class RemindItemInMemoryCache implements DataCache {

//     Map<Long, RemindItem> items = new HashMap<>();

//     @Override
//     public void putCurrentRemindItem(long botUserId, RemindItem remindItem) {
//         items.put(botUserId, remindItem);
//     }

//     @Override
//     public RemindItem getCurrentRemindItem(long botUserId) {
//         return items.get(botUserId);
//     }

//     @Override
//     public void deleteCurrentRemindItem(long botUserId) {
//         items.remove(botUserId);
//     }
    
// }
