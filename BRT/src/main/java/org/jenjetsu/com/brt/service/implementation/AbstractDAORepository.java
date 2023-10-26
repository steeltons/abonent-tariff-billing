package org.jenjetsu.com.brt.service.implementation;

import java.io.Serializable;
import static java.lang.String.format;
import java.util.Collection;
import java.util.List;

import org.jenjetsu.com.brt.exception.EntityCreateException;
import org.jenjetsu.com.brt.exception.EntityDeleteException;
import org.jenjetsu.com.brt.exception.EntityModifyException;
import org.jenjetsu.com.brt.exception.EntityNotFoundException;
import org.jenjetsu.com.brt.service.DAOService;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@SuppressWarnings("unchecked")
public abstract class AbstractDAORepository<E, ID extends Serializable>
                        implements DAOService<E, ID> {

    private final Class<? extends E> clazz;
	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager entityManager;

    protected AbstractDAORepository(Class<? extends E> clazz) {
        this.clazz = clazz;
    }

    @Override
	@Transactional
	public E create(final E raw) {
		return createEntity(raw);
	}
	
	@Override
	@Transactional
	public List<E> createAll(Collection<E> rawCollection) {
		return rawCollection.stream()
							.map((raw) -> createEntity(raw))
							.toList();
	}

	protected E createEntity(final E raw) {
		try {
			entityManager.persist(raw);
			return raw;
		} catch(Exception e) {
			String message = format("Impossible create %s", clazz.getSimpleName());
			throw new EntityCreateException(message, e);
		}
	}

	@Override
	public E readById(final ID primaryKey) {
		if(existsById(primaryKey)) {
			return (E) entityManager.find(clazz, primaryKey);
		} else {
			String message = format("Impossible find %s with primary key %s", clazz.getSimpleName(), primaryKey.toString());
			throw new EntityNotFoundException(message);
		}
	}
	
	@Override
	public List<E> readAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> cq = (CriteriaQuery<E>) cb.createQuery(clazz);
		Root<E> rootEntity = (Root<E>) cq.from(clazz);
		CriteriaQuery<E> all = (CriteriaQuery<E>) cq.select(rootEntity);
		TypedQuery<E> allQuery = (TypedQuery<E>) entityManager.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	@Transactional
	public E update(final E updatedEntity) {
		try {
			return entityManager.merge(updatedEntity);
		} catch(Exception e) {
			String message = format("Impossible modify %s", clazz.getSimpleName());
			throw new EntityModifyException(message, e);
		}
	}

	@Override
	@Transactional
	public void delete(E removableEntity) {
		try {
			if(!entityManager.contains(removableEntity)) {
				removableEntity = entityManager.merge(removableEntity);
			}
			entityManager.remove(removableEntity);
		} catch(Exception e) {
			String message = format("Impossible delete %s", clazz.getSimpleName());
			throw new EntityDeleteException(message, e);
		}
	}

	@Override
	@Transactional
	public void deleteById(ID primaryKey) {
		if(existsById(primaryKey)) {
			try {
				E entity = (E) entityManager.find(clazz, primaryKey);
				entityManager.remove(entity);
			} catch(Exception e) {
				String message = format("Impossible delete %s", clazz.getSimpleName());
				throw new EntityDeleteException(message, e);
			}
		} else {
			String message = format("Impossible delete %s with primary key %s", clazz.getSimpleName(), primaryKey.toString());
			throw new EntityNotFoundException(message);
		}
	}

	@Override
	public boolean existsById(ID primaryKey) {
		return primaryKey != null && entityManager.find(clazz, primaryKey) != null;
	}

}
