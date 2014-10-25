/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
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
    private MWUser creator;
    private String description;
    @ElementCollection
    private List<Long> dates;
    private boolean allDayEvent;
    private long duration;
    private Long deadline;
    private boolean deadlineReminder;
    @Enumerated (EnumType.STRING)
    private AnswerNotification notification;
    @OneToMany
    private List<MWUser> participators;
    
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<MWAnswer> answers;
    
    public enum AnswerNotification {
        NO_NOTIFICATION,
        EACH_ANSWER,
        LAST_ANSWER
    }
    
    public MWEvent() {
    }
    
    public MWEvent(String name, MWUser creator, String description, List<Long> dates, 
            boolean allDayEvent, long duration, Long deadline, boolean deadlineReminder, 
            AnswerNotification notification, List<MWUser> participators) {
        this.name = name;
        this.creator = creator;
        this.description=description;
        this.dates = dates;
        this.allDayEvent = allDayEvent;
        this.duration = duration;
        this.deadline = deadline;
        this.deadlineReminder = deadlineReminder;
        this.notification = notification;
        this.participators = participators;
     
    }
    
    public MWEvent(Long id, String name, MWUser creator, String description, List<Long> dates, 
            boolean allDayEvent, long duration, Long deadline, boolean deadlineReminder, 
            AnswerNotification notification, List<MWUser> participators) {
        super(id);
        this.name = name;
        this.description = description;
        this.dates = dates;
        this.allDayEvent = allDayEvent;
        this.creator = creator;
        this.duration = duration;
        this.deadline = deadline;
        this.deadlineReminder = deadlineReminder;
        this.notification = notification;
        this.participators = participators;
               
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
        
    public List<Long> getDates() {
        return dates;
    }

    public long getDuration() {
        return duration;
    }

    public Long getDeadline() {
        return deadline;
    }

    public boolean isDeadlineReminder() {
        return deadlineReminder;
    }
    public boolean isAllDayEvent(){
        return allDayEvent;
    }
    public MWUser getCreator(){
        return creator;
    }
    public AnswerNotification getNotification() {
        return notification;
    }
    
    public List<MWUser> getParticipators() {
        return participators;
    }
    
    public List<MWAnswer> getAnswers() {
        List<MWAnswer> as = new ArrayList<>();
        for (int i = (answers.size() - 1); i >= 0; i--) {
            boolean save = true;
            for (MWAnswer a2 : as) {
                if (answers.get(i).getUser().equals(a2.getUser())) {
                    save = false;
                    break;
                }
            }
            if (save) {
                as.add(answers.get(i));
            }
        }
        return as;
    }
    
    public void addAnswer(MWUser user, List<Long> dates) {
        MWAnswer answer = new MWAnswer(user, dates);
        answers.add(answer);
    }
    
    public void removeAnswer(MWAnswer answer){
        MWUser user = answer.getUser();
        for(int i =0; i < answers.size(); i++) {
            if(user.equals(answers.get(i).getUser())) {
                answers.remove(i);
                break;
            }
        }
        //answers.remove(answer); Did not seem to work...
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDates(List<Long> dates) {
        this.dates = dates;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public void setDeadlineReminder(boolean deadlineReminder) {
        this.deadlineReminder = deadlineReminder;
    }

    public void setNotification(AnswerNotification notification) {
        this.notification = notification;
    }

    public void setParticipators(List<MWUser> participators) {
        this.participators = participators;
    }

    public void setAllDayEvent(boolean allDayEvent) {
        this.allDayEvent = allDayEvent;
    }

    public void setCreator(MWUser creator) {
        this.creator = creator;
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
