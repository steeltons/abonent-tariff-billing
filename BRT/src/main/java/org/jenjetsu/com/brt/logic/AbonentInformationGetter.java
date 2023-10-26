package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.brt.dto.*;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.jenjetsu.com.brt.entity.CallOption;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.service.AbonentPayloadService;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AbonentInformationGetter {

    private final AbonentService abonentService;
    private final AbonentPayloadService abonentPayloadService;

    public AbonentInformationDTO getMyInformation(Long phoneNumber) {
        Abonent abonent = this.abonentService.readAbonentWithPayloadsByPhoneNumber(phoneNumber);
        return AbonentInformationDTO.builder()
                .abonentId(abonent.getAbonentId())
                .phoneNumber(abonent.getPhoneNumber())
                .balance(abonent.getBalance())
                .isBlocked(abonent.getIsBlocked())
                .tariff(this.convertTariff(abonent.getTariff()))
                .payloads(abonent.getPayloadList()
                                 .stream()
                                 .map(this::convertAbonentPayloads)
                                 .toList())
                .build();
    }

    public List<AbonentPayloadReturnDTO> getMyCalls(Long phoneNumber) {
        List<AbonentPayload> payloadList = this.abonentPayloadService.readByAbonentPhoneNumber(phoneNumber);
        return payloadList.stream()
                          .map(this::convertAbonentPayloads)
                          .toList();
    }

    private TariffReturnDTO convertTariff(Tariff tariff) {
        return TariffReturnDTO.builder()
                .name(tariff.getName())
                .baseCost(tariff.getBaseCost())
                .description(tariff.getDescription())
                .options(tariff.getCallOptionList()
                        .stream()
                        .map(this::convertCallOption)
                        .toList())
                .build();
    }

    private CallOptionReturnDTO convertCallOption(CallOption callOption) {
        return CallOptionReturnDTO
                .builder()
                .callType(callOption.getCallType())
                .minuteCost(callOption.getCallCost())
                .minutes(callOption.getCallBuffer())
                .optionCost(callOption.getCost())
                .callPriority(callOption.getCallPriority())
                .build();
    }
    private AbonentPayloadReturnDTO convertAbonentPayloads(AbonentPayload payload) {
        return AbonentPayloadReturnDTO
                .builder()
                .callType(payload.getCallType())
                .callTo(payload.getCallTo())
                .cost(payload.getCost())
                .duration(payload.getDuration())
                .startCallingTime(payload.getStartCallingTime().toInstant())
                .endCallingTime(payload.getEndCallingTime().toInstant())
                .build();
    }
}
