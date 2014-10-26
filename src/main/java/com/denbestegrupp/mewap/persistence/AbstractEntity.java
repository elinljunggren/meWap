
package com.denbestegrupp.mewap.persistence;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for all entities: 
 * MWEvent, MWUser and MWAnswer
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; 
   
    public Long getId(){
        return id;
    }
    
    protected AbstractEntity(){

    }
    
    protected AbstractEntity(Long id){
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final AbstractEntity other = (AbstractEntity) obj;
        return Objects.equals(this.id, other.id);
    }
   
}
