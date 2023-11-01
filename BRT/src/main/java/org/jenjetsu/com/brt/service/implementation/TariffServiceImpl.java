package org.jenjetsu.com.brt.service.implementation;

import static java.lang.String.format;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.CallOption;
import org.jenjetsu.com.brt.entity.CallOptionCard;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.exception.EntityNotFoundException;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.brt.service.TariffService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TariffServiceImpl  extends AbstractDAORepository<Tariff, UUID>
                                implements TariffService {

    private final AbonentService abonentService;

    public TariffServiceImpl(AbonentService abonentService) {
        super(Tariff.class);
        this.abonentService = abonentService;
    }

    @Override
    @Cacheable(value = "tariff", key = "#tariff.tariffId")
    public Tariff readById(UUID id) {
        return super.readById(id);
    }

    @Override
    @Cacheable(value = "abonent-tariff", key = "#phoneNumber")
    public Tariff getTariffByAbonentPhoneNumber(Long phoneNumber) {
        if(!this.abonentService.isExistByPhoneNumber(phoneNumber)) {
            throw new EntityNotFoundException(format("Abonent with phone number %d not exists", phoneNumber));
        }
        CriteriaBuilder cb = super.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tariff> cq = cb.createQuery(Tariff.class);
        Root<Abonent> abonenRoot = cq.from(Abonent.class);
        Root<Tariff> tariffRoot = cq.from(Tariff.class);
        Join<Abonent, Tariff> join = abonenRoot.join("tariff", JoinType.LEFT);
        cq.select(join)
          .where(cb.equal(abonenRoot.get("phoneNumber"), phoneNumber));
        TypedQuery<Tariff> typedQuery = this.entityManager.createQuery(cq);
        Optional<Tariff> optionalTariff = Optional.of(typedQuery.getSingleResult());
        return optionalTariff
                            .orElseThrow(() -> 
                                new EntityNotFoundException(format("""
                                        No such Abonent with phone number %d. 
                                        """, phoneNumber))
                            );
    }

    @Override
    @Transactional
    public Tariff fetchTariffById(UUID tariffId) {
        if(!super.existsById(tariffId)) {
            throw new EntityNotFoundException(format("Tariff with id %s not found", tariffId.toString()));
        }
        CriteriaBuilder cb = super.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tariff> cq = cb.createQuery(Tariff.class);
        Root<Tariff> tariffRoot = cq.from(Tariff.class);
        Fetch<Tariff, CallOptionCard> cardFetch = tariffRoot.fetch("callOptionCardList", JoinType.LEFT);
        Fetch<CallOptionCard, CallOption> inputOptionFetch = cardFetch.fetch("inputOption", JoinType.LEFT);
        Fetch<CallOptionCard, CallOption> outputOptionFetch = cardFetch.fetch("outputOption", JoinType.LEFT);
        cq.where(cb.equal(tariffRoot.get("tariffId"), tariffId));
        TypedQuery<Tariff> typedQuery = super.entityManager.createQuery(cq);
        return typedQuery.getSingleResult();
    }
}
