package ru.tyumentsev.remember_the_pills.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.tyumentsev.remember_the_pills.bot.BotState;
import ru.tyumentsev.remember_the_pills.bot.UserLocale;

@Entity
@Table(name = "bot_users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotUser {
    @Id
    @Column
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
                .append(remindItem.getName() + " " + remindItem.getStartDate() + " " + remindItem.getEndDate() + "\n"));
        return stringBuilder.toString();

    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", botState='" + getBotState() + "'" +
                "}";
    }
}
