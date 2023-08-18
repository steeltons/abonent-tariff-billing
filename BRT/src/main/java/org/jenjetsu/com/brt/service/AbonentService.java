package org.jenjetsu.com.brt.service;

import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;

import java.util.List;
import java.util.Optional;

public interface AbonentService extends CreateInterface<Abonent>,
                                        ReadInterface<Abonent, Long>,
                                        UpdateInterface<Abonent, Long>{

    public boolean isExistByPhoneNumber(Long phoneNumber);
    public Abonent getByPhoneNumber(Long phoneNumber);
    public Abonent increaseBalanceById(Long abonentId, double increment);
    public Abonent decreaseBalanceById(Long abonentId, double decrement);
    public Abonent increaseBalanceByPhoneNumber(Long phoneNumber, double increment);
    public Abonent decreaseBalanceByPhonenUmber(Long phoneNumber, double decrement);
    public List<Abonent> getAllNotBannedAbonents();
}
