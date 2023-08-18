package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.dto.RawAbonentDto;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AbonentCreator {

    public final AbonentService abonentService;

    public ResponseEntity<?> create(RawAbonentDto abonentDto) {
        Abonent abonent = new Abonent();
        abonent.setPhoneNumber(abonentDto.phoneNumber());
        abonent.setBalance(abonentDto.balance());
        Tariff tariff = new Tariff();
        tariff.setTariffCode(abonentDto.tariffCode());
        abonent.setTariff(tariff);
        abonent = abonentService.create(abonent);
        return ResponseEntity.ok(new RawAbonentDto(abonent.getAbonentId(), abonent.getPhoneNumber(), abonent.getBalance(), abonent.getTariff().getTariffCode()));
    }
}
