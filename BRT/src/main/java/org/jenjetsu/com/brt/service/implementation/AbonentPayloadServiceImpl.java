package org.jenjetsu.com.brt.service.implementation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.jenjetsu.com.brt.service.AbonentPayloadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbonentPayloadServiceImpl extends AbstractDAORepository<AbonentPayload, Long>
                                       implements AbonentPayloadService {
    
    public AbonentPayloadServiceImpl() {
        super(AbonentPayload.class);
    }

    @Override
    public List<AbonentPayload> readByAbonentPhoneNumber(Long phoneNumber) {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<AbonentPayload> cq = cb.createQuery(AbonentPayload.class);
        Root<AbonentPayload> payloadRoot = cq.from(AbonentPayload.class);
        Root<Abonent> abonentRoot = cq.from(Abonent.class);
        payloadRoot.join("abonent", JoinType.LEFT);
        cq.select(payloadRoot)
          .where(cb.equal(abonentRoot.get("phoneNumber"), phoneNumber));
        return this.entityManager.createQuery(cq).getResultList();
    }
}
