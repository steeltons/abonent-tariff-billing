package org.jenjetsu.com.hrs.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.hrs.model.*;
import org.jenjetsu.com.hrs.model.implementation.TariffImpl;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdrPlusFileParser implements Function<Resource, List<AbonentHrs>> {

    private final Function<String, CallHrs> callDeserializer;
    private final Function<String, TariffHrs> tariffDeserializer;
    private final Function<String, CallOptionHrs> callOptionDeserializer;
    private final Function<String, AbonentHrs> abonentDeserializer;
    private final Function<String, CallOptionCardHrs> cardDeserializer;

    /**
     * <h2>parseCdrPlusFile</h2>
     * <p>Parse cdr plus file into list of AbonentHrs</p>
     * @param cdrPlusFile cdr plus file
     * @return List<AbonentHrs> - parsed input abonents
     */
    @Override
    public List<AbonentHrs> apply(Resource cdrPlusFile) {
       try(BufferedReader reader = new BufferedReader(new InputStreamReader(cdrPlusFile.getInputStream()))) {
           List<CallOptionHrs> callOptionList = this.readCallOptionsFromFile(reader);
           Map<Long, CallOptionHrs> callOptionMap = callOptionList.stream()
                   .collect(Collectors.toMap(CallOptionHrs::getCallOptionId, Function.identity()));

           List<TariffHrs> tariffList = this.readTariffsFromFile(reader, callOptionMap);
           Map<UUID, TariffHrs> tariffMap = tariffList.stream()
                   .collect(Collectors.toMap(TariffHrs::getTariffId, Function.identity()));

           List<AbonentHrs> abonentList = this.readAbonentsFromFile(reader, tariffMap);
           return abonentList;
       } catch (Exception e) {
           log.error("Impossible to parse cdr.plus file");
           throw new RuntimeException(e);
       }
    }

    private List<TariffHrs> readTariffsFromFile(BufferedReader reader,
                                                Map<Long, CallOptionHrs> callOptionMap) throws IOException {
        String line;
        List<TariffHrs> list = new ArrayList<>();
        while (!(line = reader.readLine()).contains("END OF TARIFFS")) {
            TariffHrs tariff = tariffDeserializer.apply(line);
            List<CallOptionCardHrs> cardList = this.readCallOptionCardsFromFile(reader, callOptionMap)
                    .stream()
                    .sorted((c1, c2) -> c2.getCardPriority().compareTo(c1.getCardPriority()))
                    .toList();
            tariff.setCallOptionCardList(cardList);
            list.add(tariff);
        }
        return list;
    }

    private List<CallOptionHrs> readCallOptionsFromFile(BufferedReader reader) throws IOException {
        String line;
        List<CallOptionHrs> list = new ArrayList<>();
        while (!(line = reader.readLine()).isEmpty()) {
            list.add(this.callOptionDeserializer.apply(line));
        }
        return list;
    }

    private List<CallOptionCardHrs> readCallOptionCardsFromFile(BufferedReader reader,
                                                                Map<Long, CallOptionHrs> callOptionMap) throws IOException {
        String line;
        List<CallOptionCardHrs> list = new ArrayList<>();
        while ((line = reader.readLine()).startsWith("-")) {
            CallOptionCardHrs callOptionCard = this.cardDeserializer.apply(line);
            CallOptionHrs inputOption = callOptionMap.get(callOptionCard.getInputOption().getCallOptionId());
            CallOptionHrs outputOption = callOptionMap.get(callOptionCard.getOutputOption().getCallOptionId());
            callOptionCard.setInputOption(inputOption);
            callOptionCard.setOutputOption(outputOption);
            list.add(callOptionCard);
        }
        return list;
    }

    private List<AbonentHrs> readAbonentsFromFile(BufferedReader reader,
                                                   Map<UUID, TariffHrs> tariffMap) throws IOException {
        String line;
        List<AbonentHrs> list = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            AbonentHrs abonent = this.abonentDeserializer.apply(line);
            List<CallHrs> callList = this.readCallsFromFile(reader);
            abonent.setCallList(callList);
            UUID tariffId = abonent.getTariff().getTariffId();
            abonent.setTariff(tariffMap.get(tariffId));
            list.add(abonent);
        }
        return list;
    }

    private List<CallHrs> readCallsFromFile(BufferedReader reader) throws IOException{
        String line;
        List<CallHrs> list = new ArrayList<>();
        while ((line = reader.readLine()) != null && line.contains("-")) {
            list.add(callDeserializer.apply(line));
        }
        return list;
    }

}
