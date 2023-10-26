package org.jenjetsu.com.brt.service.implementation;

import static java.lang.String.format;
import java.util.List;
import java.util.UUID;

import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.exception.EntityNotFoundException;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;

@Service
public class AbonentServiceImpl extends AbstractDAORepository<Abonent, UUID>
                                implements AbonentService {

    public AbonentServiceImpl() {
        super(Abonent.class);
    }

    @Override
    public boolean isExistByPhoneNumber(Long phoneNumber) {
        if(phoneNumber == null) return false;
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Abonent> abonentRoot = cq.from(Abonent.class);
        cq.select(cb.count(abonentRoot.get("abonent_id")))
          .where(cb.equal(abonentRoot.get("phone_number"), phoneNumber));
        return this.entityManager.createQuery(cq).getSingleResult() != 0;
    }

    @Override
    public Abonent readByPhoneNumber(Long phoneNumber) {
        if(!this.isExistByPhoneNumber(phoneNumber)) {
            throw new EntityNotFoundException(format("Abonent with phone number %s not exists", phoneNumber));
        }
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Abonent> cq = cb.createQuery(Abonent.class);
        Root<Abonent> abonentRoot = cq.from(Abonent.class);
        cq.select(abonentRoot)
          .where(cb.equal(abonentRoot.get("phone_number"), phoneNumber));
        return this.entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    @Transactional
    public Abonent increaseBalanceById(UUID abonentId, float increment) {
        Abonent abonent = this.readById(abonentId);
        abonent.setBalance(abonent.getBalance() + increment);
        return abonent;
    }

    @Override
    public Abonent decreaseBalanceById(UUID abonentId, float decrement) {
        Abonent abonent = this.readById(abonentId);
        abonent.setBalance(abonent.getBalance() - decrement);
        return abonent;
    }

    @Override
    public Abonent increaseBalanceByPhoneNumber(Long phoneNumber, float increment) {
        Abonent abonent = this.readByPhoneNumber(phoneNumber);
        abonent.setBalance(abonent.getBalance() + increment);
        return abonent;
    }

    @Override
    public Abonent decreaseBalanceByPhonenUmber(Long phoneNumber, float decrement) {
        Abonent abonent = this.readByPhoneNumber(phoneNumber);
        abonent.setBalance(abonent.getBalance() - decrement);
        return abonent;
    }

    @Override
    public List<Abonent> readAllNotBannedAbonents() {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Abonent> cq = cb.createQuery(Abonent.class);
        Root<Abonent> abonentRoot = cq.from(Abonent.class);
        cq.select(abonentRoot)
          .where(cb.isFalse(abonentRoot.get("is_blocked")));
        return this.entityManager.createQuery(cq).getResultList();
    }

    @Override
    public UserDetails loadByPhoneNumber(Long phoneNumber) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadByPhoneNumber'");
    }

    @Override
    public boolean authenticateAbonent(Long phoneNumber, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateAbonent'");
    }

    @Override
    public Abonent readAbonentWithPayloadsByPhoneNumber(Long phonenUmber) {
        return null;
    }

}
