/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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

    public GoogleCal() {
        //this.cal
//        this.events = user.getEvents();

    }
    //check if mewapevent is availiable to book in users googlecalendar

    public void addIfFree(MWEvent mwEvent) {

        List<Event> eventList = events.getItems();
        List<Date> answerDates = new ArrayList<Date>();
        List<Date> suggestedDates = mwEvent.getDates();
        for (int i = 0; i < suggestedDates.size(); i++) {
            for (Event e : eventList) {
             //   if (e.) {
            //     if (e.getStart().getDateTime().getValue() == s) {

                  //  } else {
                        Event ev = new Event();
                        //should be ev.setStart(mwEvent.get(i).getStartTime()) isch
                        ev.setStart(null);
                        ev.setEnd(null);
                   //     ev.setDescription(mwEvent.get(i).getName());
                        eventList.add(ev);
                        //högst oklart hur jag får det specifika datumet på ett förslag
                //        answerDates.add(mwEvent.get(i).getDates().get(i));

//                        Date startDate = new Date();
//                        Date endDate = new Date(startDate.getTime() + 3600000);
//                        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
//                        event.setStart(new EventDateTime().setDateTime(start));
//                        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
//                        event.setEnd(new EventDateTime().setDateTime(end));

                  //  }
               // }

            }
        }
    }

    public Calendar getCal() {

        return this.cal;
    }

    public MWUser getUser() {
        return this.user;
    }

}
