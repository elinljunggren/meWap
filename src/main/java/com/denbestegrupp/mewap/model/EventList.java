/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractDAO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author emma
 */
@Stateless
public class EventList extends AbstractDAO <Event,Long> implements IEventList{

    @PersistenceContext
    private EntityManager entityManager;
    
    
    public EventList(){
        super(Event.class);
    }
    public List <Event> getByName(String name){
        List <Event> found = new ArrayList<>();
        for(Event e: findRange(0, count())){
            if(e.getName().equals(name)){
                found.add(e);
            }
        }
        return found;
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
}
