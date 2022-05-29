package ru.tyumentsev.rememberthepillsbot.entity;

import java.sql.Time;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Presents time, in which user should get notification about item.
 * 
 * @see RemindItem
 */
@Entity
@Table(name = "routine_notifications")
@Getter @Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class RoutineNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "remind_id")
    RemindItem remindItem;
    
    @Column(name = "notification_time")
    Time notificationTime;

    public RoutineNotification(long notificationTime) {
        this.notificationTime = new Time(notificationTime);
    }

    public static long getTimeLongValue(String time) {
        String hours = time.split(":")[0];
        String minutes = time.split(":")[1];
        
        long hoursLong = Long.parseLong(hours) * (60 * 60_000);
        long minutesLong = Long.parseLong(minutes) * 60_000;
        
        return hoursLong + minutesLong;
    }

}
