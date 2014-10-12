/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Oskar
 */
public class Event {
    
    private String name;
    private List<Date> dates;
    private long duration;
    private Date deadline;
    private boolean deadlineReminder;
    private AnswerNotification notification;
    
    private enum AnswerNotification {
        NO_NOTIFICATION,
        EACH_ANSWER,
        LAST_ANSWER
    }
}
