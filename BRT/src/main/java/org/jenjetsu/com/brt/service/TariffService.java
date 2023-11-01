package org.jenjetsu.com.brt.service;

import org.jenjetsu.com.brt.entity.Tariff;

import java.util.UUID;

public interface TariffService extends DAOService<Tariff, UUID>{

    public Tariff getTariffByAbonentPhoneNumber(Long phoneNumber);
    public Tariff fetchTariffById(UUID tariffId);
}
