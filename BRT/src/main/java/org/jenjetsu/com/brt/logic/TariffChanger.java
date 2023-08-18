package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.dto.ChangeTariffDto;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TariffChanger {

    private final AbonentService abonentService;

    public ResponseEntity<?> changeAbonentTariff(ChangeTariffDto tariffDto) {
        Abonent abonent = abonentService.getByPhoneNumber(tariffDto.phoneNumber());
        if(abonent.getAbonentId() == null || abonent.getTariff().getTariffCode().equals(tariffDto.newTariffId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Impossible to change tariff");
        }
        Abonent newAbonent = new Abonent();
        newAbonent.setPhoneNumber(tariffDto.phoneNumber());
        Tariff tariff = new Tariff();
        tariff.setTariffCode(tariffDto.newTariffId());
        newAbonent.setTariff(tariff);
        abonent = abonentService.updateById(newAbonent, abonent.getAbonentId());
        return ResponseEntity.ok(new ChangeTariffDto(abonent.getPhoneNumber(), abonent.getTariff().getTariffCode(), tariffDto.newTariffId()));
    }
}
