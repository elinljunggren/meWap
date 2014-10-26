/* 
 * To wrapp users 
 * 
 * @author group 1:
 *  Josefin Ondrus
 *  Emma Gustafsson
 *  Elin Ljunggren
 *  Oskar Nyberg
 */
package com.denbestegrupp.mewap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "User", propOrder = {
    "email",
    "name"
})

public class UserWrapper {
    private MWUser user;

    protected UserWrapper() { 
    }
   
    public UserWrapper(MWUser user) { 
        this.user = user; 
    }
    
    @XmlElement 
    public String getEmail() {
        return user.getEmail();
    }
    
    @XmlElement
    public String getName() {
        return user.getName();
    }

}
