/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author elin
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
