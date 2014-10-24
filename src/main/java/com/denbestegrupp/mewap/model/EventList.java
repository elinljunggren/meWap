/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author emma
 */
@Stateless
public class EventList extends AbstractDAO <MWEvent, Long> implements IEventList {

    @PersistenceContext
    private EntityManager entityManager;
    
    
    public EventList(){
        super(MWEvent.class);
    }
    
    @Override
    public List <MWEvent> getByName(String name){
        List <MWEvent> found = new ArrayList<>();
        for(MWEvent e: findRange(0, count())){
            if(e.getName().equals(name)){
                found.add(e);
            }
        }
        return found;
    }
    
    @Override
    public List<MWEvent> getRelatedToUser(MWUser user, Collection<MWEvent> es) {
        List<MWEvent> mwes = new ArrayList<>();
        
        for(MWEvent e : es) {
            if(user.equals(e.getCreator()) || e.getParticipators().contains(user)) {
                mwes.add(e); 
            } 
        }
        return mwes;
    }

    @Override
    public List<MWEvent> getHistory(Collection<MWEvent> es) {
        List<MWEvent> mwes = new ArrayList<>();
        
        for(MWEvent e : es) {
            if(e.getDeadline()<System.currentTimeMillis()) {
                mwes.add(e); 
            } 
        }
        return mwes;
    }

    @Override
    public List<MWEvent> getUpcomingEvents(Collection<MWEvent> es) {
        List<MWEvent> mwes = new ArrayList<>();
        
        for(MWEvent e : es) {
            if(e.getDeadline()>System.currentTimeMillis()) {
                mwes.add(e); 
            } 
        }
        return mwes;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
}
