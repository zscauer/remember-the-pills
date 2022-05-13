package ru.tyumentsev.remember_the_pills.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultWebhook;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.ReminderBot;

@Getter
@Setter
@Configuration
@ComponentScan(basePackages = "ru.tyumentsev.remember_the_pills")
@NoArgsConstructor
@ConfigurationProperties(prefix = "applicationconfig")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationConfig {

    String internalURL;
    String webHookURL;
    String botPath;
    String botUsername;
    String botToken; 

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(webHookURL).build();
    }

    @Bean
    public ReminderBot reminderBot(SetWebhook setWebhookInstance) throws TelegramApiException {
        ReminderBot reminderBot = new ReminderBot(setWebhookInstance, botPath, botUsername, botToken);

        DefaultWebhook defaultWebhook = new DefaultWebhook();
        defaultWebhook.setInternalUrl(internalURL);

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class, defaultWebhook);
        telegramBotsApi.registerBot(reminderBot, setWebhookInstance);

        return reminderBot;
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
