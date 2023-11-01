package org.jenjetsu.com.hrs.model.implementation;

import lombok.*;
import org.jenjetsu.com.hrs.model.CallOptionCardHrs;
import org.jenjetsu.com.hrs.model.CallOptionHrs;
import org.jenjetsu.com.hrs.model.DurationShell;
import org.jenjetsu.com.hrs.model.TariffHrs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TariffImpl implements TariffHrs, Cloneable{

    private UUID tariffId;
    private Float baseCost;
    private List<CallOptionCardHrs> callOptionCardList;

    @Override @SneakyThrows
    public TariffImpl clone() {
        List<CallOptionCardHrs> cardList = new ArrayList<>();
        if(this.callOptionCardList != null) {
            this.callOptionCardList.stream()
                .filter(Objects::nonNull)
                .sorted((c1, c2) -> c2.getCardPriority().compareTo(c1.getCardPriority()))
                .map(CallOptionCardHrs::clone)
                .forEach(cardList::add);
        }
        return TariffImpl.builder()
                .tariffId(this.tariffId != null ? UUID.fromString(this.tariffId.toString()) : null)
                .baseCost(this.baseCost != null ? Float.valueOf(this.baseCost) : null)
                .callOptionCardList(cardList)
                .build();
    }
}
