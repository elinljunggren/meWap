/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.denbestegrupp.mewap.persistence;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author elin
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
