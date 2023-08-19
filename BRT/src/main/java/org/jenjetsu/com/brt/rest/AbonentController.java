package org.jenjetsu.com.brt.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.async.BillingProcess;
import org.jenjetsu.com.brt.dto.ChangeBalanceDto;
import org.jenjetsu.com.brt.dto.ChangeTariffDto;
import org.jenjetsu.com.brt.dto.RawAbonentDto;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.logic.*;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.core.dto.PhoneNumberListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/abonent")
public class AbonentController {

    private final TariffChanger tariffChanger;
    private final BalanceChanger balanceChanger;
    private final AbonentCreator abonentCreator;
    private final AbonentInformationCollector informationCollector;
    private final AbonentService abonentService;
    private final BillingProcess billingProcess;

    @PostMapping("/create")
    public ResponseEntity<?> createAbonent(@RequestBody RawAbonentDto abonentDto) {
        return abonentCreator.create(abonentDto);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<?> getMyInformation(@PathVariable("phoneNumber") Long phoneNumber) {
        return informationCollector.collectMyInformation(phoneNumber);
    }

    @PatchMapping("/change-tariff")
    public ResponseEntity<?> changeTariff(@RequestBody ChangeTariffDto tariffDto) {
        return tariffChanger.changeAbonentTariff(tariffDto);
    }

    @PostMapping("change-balance")
    public ResponseEntity<?> changeBalance(@RequestBody ChangeBalanceDto balanceDto) {
        return balanceChanger.changeBalance(balanceDto);
    }

    @GetMapping("/calls/{phoneNumber}")
    public ResponseEntity<?> getMyCalls(@PathVariable("phoneNumber") Long phoneNumber) {
        return null;
    }

    @GetMapping("/get-not-blocked")
    public ResponseEntity<?> getNotBlockedAbonents() {
        List<Long> numbers = abonentService.getAllNotBannedAbonents().stream().map(Abonent::getPhoneNumber).collect(Collectors.toList());
        PhoneNumberListDto numberListDto = new PhoneNumberListDto(numbers);
        return ResponseEntity.ok(numberListDto);
    }

    @PatchMapping("/start-billing")
    public ResponseEntity<?> startBilling(HttpServletRequest req) {
        return billingProcess.joinToBillingProcess();
    }
}
