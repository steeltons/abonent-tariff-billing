package org.jenjetsu.com.brt.service.implementation;

import static java.lang.String.format;
import java.util.Optional;
import java.util.UUID;

import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.exception.EntityNotFoundException;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.brt.service.TariffService;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

@Service
public class TariffServiceImpl  extends AbstractDAORepository<Tariff, UUID>
                                implements TariffService {

    private final AbonentService abonentService;

    public TariffServiceImpl(AbonentService abonentService) {
        super(Tariff.class);
        this.abonentService = abonentService;
    }

    @Override
    public Tariff getTariffByAbonentPhoneNumber(Long phoneNumber) {
        if(!this.abonentService.isExistByPhoneNumber(phoneNumber)) {
            throw new EntityNotFoundException(format("Abonent with phone number %d not exists", phoneNumber));
        }
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Tariff> cq = cb.createQuery(Tariff.class);
        Root<Tariff> tariffRoot = cq.from(Tariff.class);
        Root<Abonent> abonenRoot = cq.from(Abonent.class);
        abonenRoot.join("tariff", JoinType.LEFT);
        cq.select(tariffRoot)
          .where(cb.equal(abonenRoot.get("phoneNumber"), phoneNumber));
        Optional<Tariff> optionalTariff = Optional.of(this.entityManager.createQuery(cq).getSingleResult());
        return optionalTariff
                            .orElseThrow(() -> 
                                new EntityNotFoundException(format("""
                                        No such Abonent with phone number %d. 
                                        """, phoneNumber))
                            );
    }
    
}
