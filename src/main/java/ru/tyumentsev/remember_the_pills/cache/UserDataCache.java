// package ru.tyumentsev.remember_the_pills.cache;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.stereotype.Component;

// import ru.tyumentsev.remember_the_pills.bot.BotState;

// @Component
// public class UserDataCache implements DataCache {
//     private Map<Long, BotState> usersBotStates = new HashMap<>();

//     @Override
//     public void setUsersCurrentBotState(long userId, BotState botState) {
//         usersBotStates.put(userId, botState);
//     }

//     @Override
//     public BotState getUsersCurrentBotState(long userId) {
//         BotState botState = usersBotStates.get(userId);
//         if (botState == null) {
//             botState = BotState.SHOW_MAIN_MENU;
//         }
//         return botState;
//     }
    
// }
