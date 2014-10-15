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
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Oskar
 */
@Entity
public class MWAnswer extends AbstractEntity {
    
    @Embedded
    private MWUser user;
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private List<Date> dates;
    
    public MWAnswer() {
    }
    
    public MWAnswer(MWUser user) {
        this.user = user;
	this.dates = new ArrayList();
    }
    
    public MWAnswer(MWUser user, List<Date> dates) {
        this.dates = dates;
        this.user = user;
    }
    
    public List<Date> getDates() {
	    return dates;
    }
    
    public void addDate(Date date) {
	    dates.add(date);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.user);
        hash = 89 * hash + Objects.hashCode(this.dates);
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
        final MWAnswer other = (MWAnswer) obj;
        if (!Objects.equals(this.user, other.user)
                || !Objects.equals(this.dates, other.dates)) {
            return false;
        }
        return true;
    }
    
    
        
}
