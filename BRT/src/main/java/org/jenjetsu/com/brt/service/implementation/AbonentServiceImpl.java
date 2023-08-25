package org.jenjetsu.com.brt.service.implementation;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.repository.AbonentRepository;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AbonentServiceImpl implements AbonentService {
    private final AbonentRepository abonentRep;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Abonent create(Abonent raw) {
        Abonent abonent = transactionTemplate.execute((status) -> {
            try {
                abonentRep.save(raw);
                return raw;
            } catch (Exception e) {
                throw new RuntimeException("Error create new abonent");
            }
        });
        return abonent;
    }

    @Override
    public List<Abonent> createAll(Collection<Abonent> rawCollection) {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        List<Abonent> abonents = new ArrayList<>();
        transactionTemplate.executeWithoutResult((status) -> {
            try {
                for(Abonent abonent : rawCollection) {
                    abonentRep.save(abonent);
                    abonents.add(abonent);
                }
            } catch (Exception e) {
                abonents.clear();
                throw new RuntimeException("Error create new abonents");
            }
        });
        return abonents;
    }

    public boolean isExistById(Long id) {
        return id != null && abonentRep.existsById(id);
    }

    @Override
    public Abonent readById(Long aLong) {
        if(isExistById(aLong)) {
            return abonentRep.findById(aLong).get();
        } else {
            return new Abonent();
        }
    }

    @Override
    public List<Abonent> readAll() {
        return abonentRep.findAll();
    }

    @Override
    public boolean isExistByPhoneNumber(Long phoneNumber) {
        return phoneNumber != null && abonentRep.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Abonent getByPhoneNumber(Long phoneNumber) {
        Optional<Abonent> abonent = abonentRep.findByPhoneNumber(phoneNumber);
        if(abonent.isPresent()) {
            return abonent.get();
        }
        throw new UsernameNotFoundException(String.format("Abonent with phone number %d is not exist", phoneNumber));
    }

    @Override
    public Abonent increaseBalanceById(Long id, double increment) {
        if(isExistById(id)) {
            return transactionTemplate.execute((status) -> {
                try {
                    Abonent abonent = abonentRep.findById(id).get();
                    abonent.increaseBalance(increment);
                    return abonent;
                } catch (Exception e) {
                    throw new RuntimeException("Error update abonent with id %d. Error message : %s");
                }
            });
        } else {
            throw new RuntimeException("Abonent with id is not exist");
        }
    }

    @Override
    public Abonent increaseBalanceByPhoneNumber(Long phoneNumber, double increment) {
        if(isExistByPhoneNumber(phoneNumber)) {
            return transactionTemplate.execute((status) -> {
                try {
                    Abonent abonent = abonentRep.findByPhoneNumber(phoneNumber).get();
                    abonent.increaseBalance(increment);
                    return abonent;
                } catch (Exception e) {
                    throw new RuntimeException("Error update abonent with phoneNumber %d. Error message : %s");
                }
            });
        } else {
            throw new RuntimeException("Abonent with phoneNumber %d is not exist");
        }
    }

    @Override
    public Abonent decreaseBalanceById(Long abonentId, double decrement) {
        return this.increaseBalanceById(abonentId, -decrement);
    }

    @Override
    public Abonent decreaseBalanceByPhonenUmber(Long phoneNumber, double decrement) {
        return this.increaseBalanceByPhoneNumber(phoneNumber, -decrement);
    }

    @Override
    public List<Abonent> getAllNotBannedAbonents() {
        return abonentRep.findAllNotBlockedAbonents();
    }

    @Override
    public boolean authenticateAbonent(Long phoneNumber, String password) {
        return abonentRep.validByPassword(phoneNumber, password);
    }

    public UserDetails loadByPhoneNumber(Long phoneNumber) throws UsernameNotFoundException {
        if(isExistByPhoneNumber(phoneNumber)) {
            return User.builder()
                    .username(String.format("+%d", phoneNumber))
                    .password("nopassword")
                    .authorities("ABONENT")
                    .build();
        }
        throw new UsernameNotFoundException(String.format("Abonent with number %d not found", phoneNumber));
    }

    @Override
    public Abonent updateById(Abonent newObject, Long aLong) {
        return transactionTemplate.execute((status) -> {
            if (!isExistById(aLong)) {
                return new Abonent();
            }
            try {
                Abonent updateableAbonent = abonentRep.findById(aLong).get();
                Abonent ret = updateableAbonent.update(newObject);
                return  ret;
            } catch (Exception e) {
                throw new RuntimeException("Update abonent error");
            }
        });
    }
}
