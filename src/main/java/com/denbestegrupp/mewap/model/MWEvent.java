/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractEntity;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oskar
 */
@Entity
public class MWEvent extends AbstractEntity {
    
    private String name;
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private List<Date> dates;
    private long duration;
    @Temporal(TemporalType.DATE)
    private Date deadline;
    private boolean deadlineReminder;
    @Enumerated (EnumType.STRING)
    private AnswerNotification notification;
    @ElementCollection
    private List<MWUser> participators;
    private String description;
    
    @OneToMany
    private List<MWAnswer> answers;
    
    public enum AnswerNotification {
        NO_NOTIFICATION,
        EACH_ANSWER,
        LAST_ANSWER
    }
    
    public MWEvent() {
    }
    
    public MWEvent(String name, List<Date> dates, long duration, Date deadline, boolean deadlineReminder, AnswerNotification notification, List<MWUser> participators, String description) {
        this.name = name;
        this.dates = dates;
        this.duration = duration;
        this.deadline = deadline;
        this.deadlineReminder = deadlineReminder;
        this.notification = notification;
        this.participators = participators;
        this.description=description;
    }
    
    public MWEvent(Long id, String name, List<Date> dates, long duration, Date deadline, boolean deadlineReminder, AnswerNotification notification, List<MWUser> participators, String description) {
        super(id);
        this.name = name;
        this.dates = dates;
        this.duration = duration;
        this.deadline = deadline;
        this.deadlineReminder = deadlineReminder;
        this.notification = notification;
        this.participators = participators;
        this.description = description;
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
    
    public List<MWUser> getParticipators() {
        return participators;
    }
    
    public List<MWAnswer> getAnswers() {
        return answers;
    }
    public String getDescription(){
        return description;
    }
    
    public void addAnswer(MWUser user, List<Date> dates) {
        MWAnswer answer = new MWAnswer(user, dates);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + Objects.hashCode(this.dates);
        hash = 59 * hash + (int) (this.duration ^ (this.duration >>> 32));
        hash = 59 * hash + Objects.hashCode(this.deadline);
        hash = 59 * hash + (this.deadlineReminder ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.notification);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MWEvent other = (MWEvent) obj;
        if (!Objects.equals(this.name, other.name)
                || !Objects.equals(this.dates, other.dates)
                || this.duration != other.duration
                || !Objects.equals(this.deadline, other.deadline)
                || this.deadlineReminder != other.deadlineReminder
                || this.notification != other.notification) {
            return false;
        }
        
        return true;
    }
    
    
    
}
