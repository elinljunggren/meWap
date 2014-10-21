/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author elin
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

    @XmlElement //If serving XML we should use @XmlAttribute 
    public Long getId() {
        return event.getId();
    }
    
    @XmlElement
    public List<Date> getDates() {
        return event.getDates();
    }
    @XmlElement
    public MWUser getCreator(){
        return event.getCreator();
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
    public Date getDeadline() {
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
    public List <MWUser> getParticipators(){
        return event.getParticipators();
    }
    @XmlElement
    public List<MWAnswer> getAnswers() {
        return event.getAnswers();
    }
}
