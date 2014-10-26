
package com.denbestegrupp.mewap.persistence;

import java.util.List;

/**
 * Basic contract for containers of Events and Users
 * 
 * @author Group 1:
 * Emma Gustafsson
 * Josefin Ondrus
 * Elin Ljunggren
 * Oskar Nyberg
 * 
 * @param <T> type of elements in container
 * @param <K> K is type of id (primary key)
 */
public interface IDAO<T, K> {

    public void create(T t);

    public void delete(K id);

    public void update(T t);

    public T find(K id);

    public List<T> findAll();

    public List<T> findRange(int first, int n );

    public int count();
   
}
