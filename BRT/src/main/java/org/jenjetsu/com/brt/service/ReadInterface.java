package org.jenjetsu.com.brt.service;

import java.util.List;
import java.util.Optional;

public interface ReadInterface<T, ID> {
    public T readById(ID id);
    public List<T> readAll();
}
