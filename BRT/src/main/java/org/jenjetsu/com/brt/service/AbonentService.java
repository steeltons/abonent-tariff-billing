package org.jenjetsu.com.brt.service;

import java.util.List;
import java.util.UUID;

import org.jenjetsu.com.brt.entity.Abonent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AbonentService extends DAOService<Abonent, UUID>{

    public boolean isExistByPhoneNumber(Long phoneNumber);
    public Abonent readByPhoneNumber(Long phoneNumber);
    public Abonent increaseBalanceById(UUID abonentId, float increment);
    public Abonent decreaseBalanceById(UUID abonentId, float decrement);
    public Abonent increaseBalanceByPhoneNumber(Long phoneNumber, float increment);
    public Abonent decreaseBalanceByPhonenUmber(Long phoneNumber, float decrement);
    public List<Abonent> readAllNotBannedAbonents();
    public UserDetails loadByPhoneNumber(Long phoneNumber) throws UsernameNotFoundException;
    public boolean authenticateAbonent(Long phoneNumber, String password);
    public Abonent readAbonentWithPayloadsByPhoneNumber(Long phonenUmber);
}
