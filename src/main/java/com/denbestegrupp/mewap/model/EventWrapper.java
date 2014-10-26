
package com.denbestegrupp.mewap.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A wrapper for the MWEvent 
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "Event", propOrder = {
    "id",
    "name",
    "creator",
    "description",
    "dates",
    "allDayEvent",
    "duration",
    "deadline",
    "deadlineReminder",
    "notification",
    "participators",
    "answers"
})
public class EventWrapper {
    
    private MWEvent event;

    protected EventWrapper() { // Must have
    }
   
    public EventWrapper(MWEvent event) { 
        this.event = event; 
    }
    
    @XmlElement
    public String getName() {
        return event.getName();
    }

    @XmlElement
    public Long getId() {
        return event.getId();
    }
    
    @XmlElement
    public Collection<Long> getDates() {
        return event.getDates();
    }
    
    @XmlElement
    public UserWrapper getCreator(){
        return new UserWrapper(event.getCreator());
    }
    
    @XmlElement
    public String getDescription(){
        return event.getDescription();
    }
    
    @XmlElement
    public boolean isAllDayEvent(){
        return event.isAllDayEvent();
    }
    
    @XmlElement
    public long getDuration() {
        return event.getDuration();
    }

    @XmlElement
    public Long getDeadline() {
        return event.getDeadline();
    }

    @XmlElement
    public boolean isDeadlineReminder() {
        return event.isDeadlineReminder();
    }

    @XmlElement
    public MWEvent.AnswerNotification getNotification() {
        return event.getNotification();
    }
    
    @XmlElement
    public Collection<UserWrapper> getParticipators(){
        Collection<MWUser> u = event.getParticipators();
        Collection<UserWrapper> cu = new ArrayList<>();
        for (MWUser user : u){
            cu.add(new UserWrapper(user));
        }
        return cu;
    }
    
    @XmlElement
    public Collection<AnswerWrapper> getAnswers() {
        Collection<MWAnswer> u = event.getAnswers();
        Collection<AnswerWrapper> cu = new ArrayList<>();
        for (MWAnswer answer : u){
            cu.add(new AnswerWrapper(answer));
        }
        return cu;
    }
}
