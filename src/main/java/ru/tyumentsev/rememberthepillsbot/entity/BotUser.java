package ru.tyumentsev.rememberthepillsbot.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.rememberthepillsbot.bot.BotState;
import ru.tyumentsev.rememberthepillsbot.bot.UserLocale;

/**
 * Presents bot user with his locale, state and items.
 * 
 * @see UserLocale
 * @see BotState
 * @see RemindItem
 */
@Entity
@Table(name = "bot_users")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "remindItems")
@ToString(exclude = { "userLocale", "remindItems" })
public class BotUser {
    @Id
    @Column(updatable = false)
    long id;

    @Column(name = "locale")
    @Enumerated(EnumType.STRING)
    UserLocale userLocale;

    @Column(name = "bot_state")
    @Enumerated(EnumType.STRING)
    BotState botState;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bot_user_id")
    @OrderBy("id")
    Set<RemindItem> remindItems;

    public BotUser(long id) {
        this.id = id;
    }

    public void addRemindItem(RemindItem remindItem) {
        remindItems.add(remindItem);
    }

    public String getListOfRemindItems() {
        StringBuilder stringBuilder = new StringBuilder();
        remindItems.stream().forEach(remindItem -> stringBuilder
                .append(remindItem.getName() + " "
                        + LocalDate.ofInstant(remindItem.getStartDate().toInstant(), ZoneId.systemDefault()) + " - "
                        + LocalDate.ofInstant(remindItem.getEndDate().toInstant(), ZoneId.systemDefault()) + "\n"));
        return stringBuilder.toString();

    }
}
