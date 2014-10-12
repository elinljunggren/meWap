/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Embeddable;

/**
 *
 * @author Oskar
 */
@Embeddable
public class Answer extends ArrayList<Date> {

    private User user;
    
    public Answer() {
    }
    
    public Answer(User user) {
        super();
        this.user = user;
    }
    
    public Answer(User user, List<Date> dates) {
        super(dates);
        this.user = user;
    }
        
}
