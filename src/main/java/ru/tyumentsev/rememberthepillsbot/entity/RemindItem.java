package ru.tyumentsev.rememberthepillsbot.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Presents items for notification about them.
 * 
 * @see RoutineNotification
 */
@Entity
@Table(name = "reminds")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "notifications")
public class RemindItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "bot_user_id")
    BotUser botUser;

    @Column(name = "remind_name")
    String name;

    @Column(name = "start_date")
    Date startDate;

    @Column(name = "end_date")
    Date endDate;

    /**
     * Collection of notifications, each of those provides time of notify about this item.
     */
    @OneToMany(mappedBy = "remindItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<RoutineNotification> notifications;

    public RemindItem(BotUser botUser) {
        this.botUser = botUser;
    }

    public void addNotification(RoutineNotification notification) {
        notification.setRemindItem(this);
        notifications.add(notification);
    }
}
