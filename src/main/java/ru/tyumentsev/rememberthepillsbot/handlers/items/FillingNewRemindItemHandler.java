package ru.tyumentsev.rememberthepillsbot.handlers.items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.RemindItemState;
import ru.tyumentsev.rememberthepillsbot.cache.RemindItemCacheEntity;
import ru.tyumentsev.rememberthepillsbot.entity.BotUser;
import ru.tyumentsev.rememberthepillsbot.entity.RemindItem;
import ru.tyumentsev.rememberthepillsbot.handlers.UserRequestHandler;
import ru.tyumentsev.rememberthepillsbot.service.BotUserService;
import ru.tyumentsev.rememberthepillsbot.service.RemindItemCacheService;
import ru.tyumentsev.rememberthepillsbot.service.RemindItemService;
import ru.tyumentsev.rememberthepillsbot.service.ReplyMessageService;

/**
 * Handle user request of adding new remind.
 * 
 * @see UserRequestHandler
 * @see RemindItem
 * @see RemindItemCacheEntity
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FillingNewRemindItemHandler implements UserRequestHandler {
    
    ReplyMessageService replyMessageService;
    RemindItemService remindItemService;
    BotUserService botUserService;
    RemindItemCacheService remindItemCacheService;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public SendMessage handle(BotUser botUser, BotApiObject botApiObject) {
        String userAnswer;
        long chatId;

        // get chat id to save user answers in cache.
        if (botApiObject.getClass() == CallbackQuery.class) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            userAnswer = callbackQuery.getMessage().getText();
            chatId = callbackQuery.getMessage().getChatId();
        } else {
            Message message = (Message) botApiObject;
            userAnswer = message.getText();
            chatId = message.getChatId();
        }
       
        RemindItemState remindItemState = null;
        // get user previous answers.
        RemindItemCacheEntity remindItemCacheEntity = remindItemCacheService.findByChatId(chatId);

        if (remindItemCacheEntity == null) {
            remindItemCacheEntity = new RemindItemCacheEntity();
            remindItemCacheEntity.setChatId(chatId);
            remindItemState = RemindItemState.NEW_ITEM;
        } else {
            remindItemState = remindItemCacheEntity.getRemindItemState();
        }

        // define which field user filling with this value and save it to cache.
        switch (remindItemState) {
            case NEW_ITEM:
                remindItemCacheEntity.setRemindItemState(RemindItemState.ITEM_SET_NAME);
                remindItemCacheService.save(remindItemCacheEntity);
                break;
            case ITEM_SET_NAME:
                remindItemCacheEntity.setName(userAnswer);
                remindItemCacheEntity.setRemindItemState(RemindItemState.ITEM_SET_START_DATE);
                remindItemCacheService.save(remindItemCacheEntity);
                break;
            case ITEM_SET_START_DATE:
                try {
                    Date startDate = simpleDateFormat.parse(userAnswer);
                    remindItemCacheEntity.setStartDate(startDate);
                } catch (ParseException e) {
                    return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                            RemindItemState.ERROR_INCORRECT_DATE_FORMAT);
                } catch (Exception e) {
                    remindItemCacheService.delete(remindItemCacheEntity);
                    botUser.setBotState(BotState.SHOW_USER_INFO);
                    botUserService.save(botUser);
                    return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                            e.getLocalizedMessage());
                }
                remindItemCacheEntity.setRemindItemState(RemindItemState.ITEM_SET_END_DATE);
                remindItemCacheService.save(remindItemCacheEntity);
                break;
            case ITEM_SET_END_DATE:
                try {
                    Date endDate = simpleDateFormat.parse(userAnswer);
                    remindItemCacheEntity.setEndDate(endDate);
                    saveNewItem(remindItemCacheEntity, botUser);
                } catch (ParseException e) {
                    return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                            RemindItemState.ERROR_INCORRECT_DATE_FORMAT);
                } catch (Exception e) {
                    botUser.setBotState(BotState.SHOW_USER_INFO);
                    botUserService.save(botUser);
                    return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                            e.getLocalizedMessage());
                }
                break;
            default:
                break;
        }

        return replyMessageService.getReplyMessage(botUser, String.valueOf(chatId),
                remindItemCacheEntity.getRemindItemState());


    }

    private void saveNewItem(RemindItemCacheEntity remindItemCacheEntity, BotUser botUser) {
        RemindItem userRemindItem = remindItemService.create(remindItemCacheEntity, botUser);
        botUser.addRemindItem(userRemindItem);
        botUser.setBotState(BotState.SHOW_USER_INFO);
        botUserService.save(botUser);
        remindItemCacheService.delete(remindItemCacheEntity);
        remindItemCacheEntity.setRemindItemState(RemindItemState.ITEM_SUCCESSFULLY_ADDED);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ITEM_ADD;
    }

}
