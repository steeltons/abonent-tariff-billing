package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.dto.ChangeBalanceDto;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class BalanceChanger {

    private final AbonentService abonentService;

    public ResponseEntity<?> changeBalance(ChangeBalanceDto balanceDto) {
        Abonent abonent = abonentService.getByPhoneNumber(balanceDto.phoneNumber());
        if(abonent.getAbonentId() == null || abonent.getBalance().equals(balanceDto.newBalance())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Impossible to change abonent balance");
        }
        Abonent newAbonent = new Abonent(null, null, balanceDto.newBalance(), null, new ArrayList<>(), false);
        abonent = abonentService.updateById(newAbonent, abonent.getAbonentId());
        return ResponseEntity.ok(new ChangeBalanceDto(abonent.getPhoneNumber(), abonent.getBalance(), balanceDto.newBalance()));
    }
}
