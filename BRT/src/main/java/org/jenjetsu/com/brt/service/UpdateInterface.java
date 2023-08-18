package org.jenjetsu.com.brt.service;

import java.util.Optional;

public interface UpdateInterface<T, ID> {

    public T updateById(final T newObject, ID id);
}
