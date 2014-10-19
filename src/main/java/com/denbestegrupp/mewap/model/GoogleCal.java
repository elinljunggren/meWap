/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;


import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Embeddable;

/**
 *
 * @author emma
 */
@Embeddable
public class GoogleCal {

    private Calendar cal;
    private Events events;
    private MWUser user;
    private String todays;
    
    public GoogleCal() {
        //this.cal
//        this.events = user.getEvents();

    }

    public List<Date> getFreeDates(MWEvent mwEvent) {
        List<Date> answerDates = new ArrayList<Date>();
        List<Date> mwDates = mwEvent.getDates();
        for (int i = 0; i < mwDates.size(); i++) {
            if (isFree(mwDates.get(i))) {
                answerDates.add(mwDates.get(i));
            }
        }
        return answerDates;
    }

    //check if mewapevent is availiable to book in users googlecalendar
    public boolean isFree(Date date) {

        List<Event> eventList = events.getItems();
        int high = eventList.size() - 1;
        int low = 0;
        int half = (low + high) / 2;

        while (low <= high) {
            if (eventList.get(half).getStart().getDate().getValue() == (date.getTime())) {
                return false;

            } else if (eventList.get(half).getStart().getDate().getValue() > (date.getTime())) {
                high = half - 1;
            } else {
                low = half + 1;
            }
            half = (low + high) / 2;

        }

        return true;
    }


    public Calendar getCal() {

        return this.cal;
    }

    public MWUser getUser() {
        return this.user;
    }
    public String getTodaysDate(){
        DateFormat dateForm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        todays = dateForm.format(date);
        return todays;
    }

}