package ru.tyumentsev.rememberthepillsbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.tyumentsev.rememberthepillsbot.bot.ReminderBot;

@RestController
@RequestMapping("callback/reminder")
public class WebhookController {
    
    @Autowired
    private ReminderBot reminderBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return reminderBot.onWebhookUpdateReceived(update);
    }
}
