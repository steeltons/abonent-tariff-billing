package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.brt.dto.*;
import org.jenjetsu.com.brt.entity.*;
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

    /**
     * <h2>getMyInformation</h2>
     * <p>Fetch tariff (all options), abonent calls and abonent information and return them</p>
     * @param phoneNumber - abonent number
     * @return AbonentInformationDTO
     */
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

    /**
     * <h2>getMyCalls</h2>
     * <p>Load abonent calls and return them</p>
     * @param phoneNumber - abonent phone number
     * @return List<AbonentPayloadReturnDTO>
     */
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
                .cards(tariff.getCallOptionCardList().stream()
                        .map(this::convertCallOptionCard)
                        .toList())
                .build();
    }

    private CallOptionCardReturnDTO convertCallOptionCard(CallOptionCard card) {
        return CallOptionCardReturnDTO.builder()
                .cardId(card.getCallOptionCardId())
                .inputOption(this.convertCallOption(card.getInputOption()))
                .outputOption(this.convertCallOption(card.getOutputOption()))
                .cardCost(card.getCardCost())
                .sharedBuffer(card.getSharedMinuteBuffer())
                .cardPriority(card.getCardPriority())
                .build();
    }



    private CallOptionReturnDTO convertCallOption(CallOption callOption) {
        return CallOptionReturnDTO.builder()
                .minuteCost(callOption.getMinuteCost())
                .minuteBuffer(callOption.getMinuteBuffer())
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
