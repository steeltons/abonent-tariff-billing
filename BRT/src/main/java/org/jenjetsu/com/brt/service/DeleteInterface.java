package org.jenjetsu.com.brt.service;

import java.util.Optional;

public interface DeleteInterface<T, ID> {

    public Optional<T> deleteById(ID id);
    public T deleteAll();
}
