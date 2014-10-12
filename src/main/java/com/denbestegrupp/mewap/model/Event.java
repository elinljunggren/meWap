/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

/**
 *
 * @author Oskar
 */
@Entity
public class Event extends AbstractEntity {
    
    private String name;
    @OneToMany
    private List<Date> dates;
    private long duration;
    private Date deadline;
    private boolean deadlineReminder;
    @Enumerated (EnumType.STRING)
    private AnswerNotification notification;
    
    private enum AnswerNotification {
        NO_NOTIFICATION,
        EACH_ANSWER,
        LAST_ANSWER
    }
    
    public Event() {}

    public Event(String name, List<Date> dates, long duration, Date deadline, boolean deadlineReminder, AnswerNotification notification) {
        this.name = name;
        this.dates = dates;
        this.duration = duration;
        this.deadline = deadline;
        this.deadlineReminder = deadlineReminder;
        this.notification = notification;
    }

    public String getName() {
        return name;
    }

    public List<Date> getDates() {
        return dates;
    }

    public long getDuration() {
        return duration;
    }

    public Date getDeadline() {
        return deadline;
    }

    public boolean isDeadlineReminder() {
        return deadlineReminder;
    }

    public AnswerNotification getNotification() {
        return notification;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setDeadlineReminder(boolean deadlineReminder) {
        this.deadlineReminder = deadlineReminder;
    }

    public void setNotification(AnswerNotification notification) {
        this.notification = notification;
    }
    
}
