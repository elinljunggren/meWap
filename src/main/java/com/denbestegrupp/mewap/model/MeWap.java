
package com.denbestegrupp.mewap.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

/**
 * This class represents the meWap system
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 */
@ApplicationScoped
public class MeWap implements IMeWap {
    
    @EJB
    private IEventList eventList;
    @EJB
    private IUserList userList;
    
    protected MeWap() {
        Logger.getAnonymousLogger().log(Level.INFO, "MeWap alive");
    }

    @Override
    public IEventList getEventList() {
        return eventList;
    }

    @Override
    public IUserList getUserList() {
        return userList;
    }
    
}
