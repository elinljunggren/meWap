/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author josefinondrus
 */
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
