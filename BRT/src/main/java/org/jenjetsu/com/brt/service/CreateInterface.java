package org.jenjetsu.com.brt.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CreateInterface<T> {

    public T create(T raw);
    public List<T> createAll(Collection<T> rawCollection);
}
