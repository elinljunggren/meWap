package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
* A class which represents an answer to an event
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Entity
public class MWAnswer extends AbstractEntity {
    
    private MWUser user;
    @ElementCollection
    private List<Long> dates;
    
    public MWAnswer() {
    }
    
    public MWAnswer(MWUser user) {
        this.user = user;
        this.dates = new ArrayList();
    }
    
    public MWAnswer(MWUser user, List<Long> dates) {
        this.dates = dates;
        this.user = user;
    }
    
    public MWUser getUser() {
	    return user;
    }
    
    public List<Long> getDates() {
	    return dates;
    }
    
    public void addDate(Long date) {
	    dates.add(date);
    }
    
    public void updateDates(List<Long> newDates) {
        System.out.println("DETTA ÄR GAMLA DATUMEN: " + this.dates.toString());
        this.dates = newDates;
        System.out.println("DETTA ÄR NYA DATUMEN: " + this.dates.toString());
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
