package org.jenjetsu.com.brt.service;

import org.jenjetsu.com.brt.entity.AbonentPayload;

import java.util.List;

public interface AbonentPayloadService extends DAOService<AbonentPayload, Long>{

    public List<AbonentPayload> readByAbonentPhoneNumber(Long phoneNumber);
}
