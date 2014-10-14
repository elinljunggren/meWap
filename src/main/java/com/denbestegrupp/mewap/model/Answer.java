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
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oskar
 */
@Entity
public class Answer extends AbstractEntity {

    private User user;
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private List<Date> dates;
    
    public Answer() {
    }
    
    public Answer(User user) {
        this.user = user;
	this.dates = new ArrayList();
    }
    
    public Answer(User user, List<Date> dates) {
        this.dates = dates;
        this.user = user;
    }
    
    public List<Date> getDates() {
	    return dates;
    }
    
    public void addDate(Date date) {
	    dates.add(date);
    }
        
}
