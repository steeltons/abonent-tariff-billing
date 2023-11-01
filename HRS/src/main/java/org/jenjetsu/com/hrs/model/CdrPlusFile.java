package org.jenjetsu.com.hrs.model;

import lombok.*;
import org.jenjetsu.com.hrs.model.implementation.AbonentImpl;
import org.jenjetsu.com.hrs.model.implementation.TariffImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CdrPlusFile {

    private Map<UUID, TariffImpl> tariffMap;
    private List<AbonentImpl> anonentList;
}
