package ru.tyumentsev.remember_the_pills.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "reminds")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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

    @OneToMany(mappedBy = "remindItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JoinColumn(name = "remind_id")
    Set<RoutineNotification> notifications;

    public RemindItem(BotUser botUser) {
        this.botUser = botUser;
    }

    public void addNotification(RoutineNotification notification) {
        // String hours = time.split(":")[0];
        // String minutes = time.split(":")[1];
        // long hoursLong = Long.parseLong(hours) * (60 * 60_000);
        // long minutesLong = Long.parseLong(minutes) * 60_000;
        // RoutineNotification notification = new RoutineNotification(RoutineNotification.getTimeLongValue(time));
        notification.setRemindItem(this);
        notifications.add(notification);
    }
}
