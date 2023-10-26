package org.jenjetsu.com.brt.service;

import java.io.Serializable;
import java.util.Collection;

public interface  DAOService<E, ID extends Serializable> {
    
    public E create(E raw);
    public Collection<E> createAll(Collection<E> rawCollection);
    public E readById(ID id);
    public Collection<E> readAll();
    public void delete(E deleteCollection);
    public void deleteById(ID id);
    public E update(E newEntity);
    public boolean existsById(ID id);
}
