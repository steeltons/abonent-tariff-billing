package org.jenjetsu.com.brt.service;

import org.jenjetsu.com.brt.entity.Tariff;

public interface TariffService extends CreateInterface<Tariff>,
                                       ReadInterface<Tariff, Integer>,
                                       UpdateInterface<Tariff, Integer>{

    public boolean isExistById(Integer id);
    public Tariff getTariffByAbonentPhoneNumber(Long phoneNumber);
}
