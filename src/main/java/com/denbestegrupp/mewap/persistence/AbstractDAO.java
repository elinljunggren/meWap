
package com.denbestegrupp.mewap.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * A container for entities, base class for EventList and UserList.
 * The fundamental common operations are here. 
 *
 * T is type for items in container, K is type of id (primary key)
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 * 
 * @param <T> Any type
 * @param <K> Key
 */

public abstract class AbstractDAO<T, K> implements IDAO<T, K> {

    private final Class<T> clazz;
    
    
    protected AbstractDAO(Class<T> clazz) {
        this.clazz = clazz;
    }
  
    protected abstract EntityManager getEntityManager();
    
    @Override
    public void create(T t) {
        getEntityManager().persist(t);
    }

    @Override
    public void delete(K id) {
        T t = getEntityManager().getReference(clazz, id);
        getEntityManager().remove(t);
    }

    @Override
    public void update(T t) {
        getEntityManager().merge(t);
    }

    @Override
    public T find(K id) {
        return getEntityManager().find(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return getEntityManager().createQuery("select t from " + clazz.getSimpleName() + " t", clazz).getResultList();
    }

    @Override
    public List<T> findRange(int first, int n) {
        TypedQuery<T> q = getEntityManager().createQuery("select t from " + clazz.getSimpleName() + " t", clazz);
        q.setFirstResult(first);
        q.setMaxResults(n);
        
        return q.getResultList();
       
    }

    @Override
    public int count() {
        return getEntityManager().createQuery("select count(t) from " + clazz.getSimpleName() + " t", Long.class)
                .getSingleResult().intValue();
    }
}
