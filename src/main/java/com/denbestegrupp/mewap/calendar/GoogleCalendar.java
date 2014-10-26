/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.calendar;

import com.denbestegrupp.mewap.auth.AuthConstants;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Oskar
 */
public class GoogleCalendar {
    
    Logger log = Logger.getAnonymousLogger();
    
    Calendar calendar;
    private Object OAuth2Native;
    
    public GoogleCalendar() {
    }
    
    public List<String> getEvents(Long start, Long end, HttpHeaders hh) {
        calendar = createCalendar(createCredentials(hh));
        List<String> eventsString = new ArrayList<>();
        
        try {
            List<CalendarListEntry> calendars = calendar.calendarList().list().execute().getItems();
            for (CalendarListEntry c : calendars) {
                List<Event> events = calendar.events().list(c.getId())
                                    .set("timeMin", new DateTime(getStartOfDay(start)))
                                    .set("timeMax", new DateTime(getEndOfDay(end)))
                                    .execute().getItems();
                for (Event e : events) {
                    eventsString.add(e.toString());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return eventsString;
    }
    
    private Calendar createCalendar(Credential credential) {
        
        try {
            calendar = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                    .setApplicationName("meWap").build();
        } catch (GeneralSecurityException | IOException ex) {
            ex.printStackTrace();
        }
        return calendar;
    }
    
    private Credential createCredentials(HttpHeaders hh) {
        Credential credential = new GoogleCredential.Builder()
                        .setTransport(new NetHttpTransport())
                        .setJsonFactory(new JacksonFactory())
                        .setClientSecrets(AuthConstants.CLIENT_ID, AuthConstants.CLIENT_SECRET)
                        .build();
        credential.setAccessToken(hh.getCookies().get("access_token").getValue());
        credential.setRefreshToken(hh.getCookies().get("refresh_token").getValue());
        return credential;
    }
    
    private Date getEndOfDay(long timestamp) {
        Date date = new Date(timestamp);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        c.set(java.util.Calendar.HOUR_OF_DAY, 23);
        c.set(java.util.Calendar.MINUTE, 59);
        c.set(java.util.Calendar.SECOND, 59);
        c.set(java.util.Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    private Date getStartOfDay(long timestamp) {
        Date date = new Date(timestamp);
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
}
