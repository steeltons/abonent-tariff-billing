package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.dto.AbonentInformationDto;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AbonentInformationCollector {

    private final AbonentService abonentService;

    public ResponseEntity<?> collectMyInformation(Long phoneNumber) {
        AbonentInformationDto informationDto = new AbonentInformationDto();
        if(abonentService.isExistByPhoneNumber(phoneNumber)) {
            Abonent abonent = abonentService.getByPhoneNumber(phoneNumber);
            informationDto.setPhoneNumber(abonent.getPhoneNumber());
            informationDto.setMyId(abonent.getAbonentId());
            informationDto.setCurrentTariff(abonent.getTariff());
            informationDto.setMyPayloads(abonent.getPayloads());
            informationDto.setBalance(abonent.getBalance());
            return ResponseEntity.ok(informationDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("We cannot find you in our database");
        }
    }
}
