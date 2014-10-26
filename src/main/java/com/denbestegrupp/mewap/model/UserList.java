package com.denbestegrupp.mewap.model;

import com.denbestegrupp.mewap.persistence.AbstractDAO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
* A class which makes Users accessible from database
* @author Group 1:
* Emma Gustafsson
* Josefin Ondrus
* Elin Ljunggren
* Oskar Nyberg
*/
@Stateless
public class UserList extends AbstractDAO <MWUser, String> implements IUserList {

    @PersistenceContext
    private EntityManager entityManager;

    public UserList() {
        super(MWUser.class);
    }
    
    @Override
    public List<MWUser> getByName(String name) {
        List <MWUser> found = new ArrayList<>();
        for(MWUser e: findRange(0, count())){
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
