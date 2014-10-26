package com.denbestegrupp.mewap.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
* A class representing an user
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Entity
public class MWUser implements Serializable {
    
    @Id
    private String email;
    private String name;

    public MWUser() {
    }
    
    public MWUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final MWUser other = (MWUser) obj;
        if (!Objects.equals(this.email, other.email)
                || !Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
