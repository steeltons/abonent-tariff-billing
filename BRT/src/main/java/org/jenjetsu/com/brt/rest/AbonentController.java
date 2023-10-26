package org.jenjetsu.com.brt.rest;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.async.BillingProcess;
import org.jenjetsu.com.brt.dto.*;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.logic.*;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/abonent")
public class AbonentController {
    private final AbonentInformationGetter informationCollector;
    private final BillingProcess billingProcess;
    private final AbonentService abonentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAbonent(@RequestBody RawAbonentDTO dto) {
        this.abonentService
                .create(Abonent.builder()
                .phoneNumber(dto.phoneNumber())
                .balance(dto.balance())
                .isBlocked(false)
                .tariff(Tariff.builder().tariffId(dto.tariffId()).build())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<?> getMyInformation(@PathVariable("phoneNumber") Long phoneNumber) {
        AbonentInformationDTO dto = this.informationCollector.getMyInformation(phoneNumber);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(dto);
    }

    @PatchMapping("/change-tariff")
    public ResponseEntity<?> changeTariff(@RequestBody ChangeTariffDTO tariffDto) {
        Abonent abonent = this.abonentService.readByPhoneNumber(tariffDto.phoneNumber());
        abonent.setTariff(Tariff.builder().tariffId(tariffDto.newTariffId()).build());
        this.abonentService.update(abonent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-balance")
    public ResponseEntity<?> changeBalance(@RequestBody ChangeBalanceDTO balanceDto) {
        Abonent abonent = this.abonentService.readByPhoneNumber(balanceDto.phoneNumber());
        ChangeBalanceDTO returnDto = new ChangeBalanceDTO(balanceDto.phoneNumber(), balanceDto.sum(), abonent.getBalance());
        abonent.setBalance(balanceDto.newBalance());
        this.abonentService.update(abonent);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(returnDto);
    }

    @GetMapping("/calls/{phoneNumber}")
    public ResponseEntity<?> getMyCalls(@PathVariable("phoneNumber") Long phoneNumber) {
        List<AbonentPayloadReturnDTO> listDto = this.informationCollector.getMyCalls(phoneNumber);
        AbonentPayloadListDTO dto = new AbonentPayloadListDTO(phoneNumber, listDto);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(dto);
    }

    @PatchMapping("/start-billing")
    public ResponseEntity<?> startBilling(BillCommandDto commandDto) {
        if(!commandDto.command().equals("generate") && !commandDto.command().equals("collect-and-send")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new MessageDto("Not correct command to bill abonents."));
        }
        return billingProcess.joinToBillingProcess(commandDto.command());
    }
}
