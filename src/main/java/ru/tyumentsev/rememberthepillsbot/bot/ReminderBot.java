package ru.tyumentsev.rememberthepillsbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReminderBot extends SpringWebhookBot {
    
    final String botPath;
    final String botUsername;
    final String botToken;

    @Autowired
    private BotFacade botFacade;

    public ReminderBot(SetWebhook setWebhook, String botPath, String botUsername, String botToken) {
        super(setWebhook);
        this.botPath = botPath;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> replyToUser = botFacade.handleUpdate(update);
        return replyToUser;
    }

    


}