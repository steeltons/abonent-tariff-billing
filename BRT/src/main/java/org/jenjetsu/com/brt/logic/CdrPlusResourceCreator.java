package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.CallOption;
import org.jenjetsu.com.brt.entity.CallOptionCard;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.brt.service.TariffService;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class CdrPlusResourceCreator implements Function<Map<Long, List<CallInformation>>, Resource>{

    private final TariffService tariffService;
    private final Function<CallOptionCard, String> callOptionCardSerializer;
    private final Function<CallOption, String> callOptionSerializer;
    private final Function<CallInformation, String> callInformationSerializer;
    private final Function<Tariff, String> tariffSerializer;
    private final Function<Abonent, String> abonentSerializer;

    /**
     * <h2>createCdrPlusFile</h2>
     * <p>Convert abonent calls to binary file.</p>
     * <p>Output example:</p>
     * <code>call_option_id_1 minute_cost minute_buffer<br>
     * ...................................................................<br>
     * call_option_id_N minute_cost minute_buffer<br>
     * <br>
     * tariff_id_1 base_cost<br>
     * - tariff_id_1 input_option_id output_option_id shared_buffer card_priority card_cost<br>
     * ...................................................................<br>
     * - tariff_id_N input_option_id output_option_id shared_buffer card_priority card_cost<br>
     * ...................................................................<br><br>
     * END OF TARIFFS<br>
     * phone_number_1 tariff_id<br>
     * - call_type call_to start_calling_time end_calling_time<br>
     * ...................................................................<br>
     * - call_type call_to start_calling_time end_calling_time<br>
     * ..................................................................<br>
     * phone_number_N tariff_id<br>
     * ...................................................................</code>
     * @param abonentCallMap - map, grouped by phone numbers
     * @return ByteArrayOutputResource
     */
    public Resource apply(Map<Long, List<CallInformation>> abonentCallMap) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<Long, UUID> abonentTariffMap = new HashMap<>();
            List<Tariff> tariffList = abonentCallMap.keySet().stream()
                    .map((phoneNumber) -> {
                            Tariff tariff = this.tariffService.getTariffByAbonentPhoneNumber(phoneNumber);
                            abonentTariffMap.put(phoneNumber, tariff.getTariffId());
                            return tariff;
                        })
                    .filter(distinctByKey(Tariff::getTariffId))
                    .map((tariff) -> this.tariffService.fetchTariffById(tariff.getTariffId()))
                    .toList();
            List<CallOption> callOptionList = tariffList.stream()
                    .flatMap((tariff) -> tariff.getCallOptionCardList().stream())
                    .flatMap((card) -> Stream.of(card.getInputOption(), card.getOutputOption()))
                    .filter(Objects::nonNull)
                    .filter(distinctByKey(CallOption::getCallOptionId))
                    .toList();
            this.writeCallOption(callOptionList, outputStream);
            this.writeTariffWithCards(tariffList, outputStream);
            this.writeAbonentsWithCalls(abonentCallMap, abonentTariffMap, outputStream);
            return new ByteArrayResource(outputStream.toByteArray()) {
                private final String filename = UUID.randomUUID().toString() + ".cdrplus";
                public String getFilename() {
                    return filename;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Impossible to write cdrplus file.", e);
        }
    }

    private static <T>Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private void writeCallOption(List<CallOption> callOptionList, OutputStream outputStream) throws Exception{
        Iterator<CallOption> iterator = callOptionList.listIterator();
        while (iterator.hasNext()) {
            CallOption callOption = iterator.next();
            outputStream.write((this.callOptionSerializer.apply(callOption) + "\n").getBytes());
        }
        outputStream.write((byte) '\n');
    }

    private void writeTariffWithCards(List<Tariff> tariffList, OutputStream outputStream) throws Exception{
        Iterator<Tariff> tariffIterator = tariffList.listIterator();
        while (tariffIterator.hasNext()) {
            Tariff tariff = tariffIterator.next();
            outputStream.write((this.tariffSerializer.apply(tariff) + "\n").getBytes());
            Iterator<CallOptionCard> cardIterator = tariff.getCallOptionCardList().listIterator();
            while (cardIterator.hasNext()) {
                CallOptionCard card = cardIterator.next();
                outputStream.write(this.callOptionCardSerializer.apply(card).getBytes());
                outputStream.write((byte) '\n');
            }
            outputStream.write((byte) '\n');
        }
        outputStream.write("END OF TARIFFS\n".getBytes());
    }

    private void writeAbonentsWithCalls(Map<Long, List<CallInformation>> abonentCalls,
                                        Map<Long, UUID> abonentTariffMap,
                                        OutputStream outputStream) throws Exception{
        Iterator<Entry<Long, List<CallInformation>>> abonentIterator = abonentCalls.entrySet().iterator();
        while (abonentIterator.hasNext()) {
            Entry<Long, List<CallInformation>> abonentPair = abonentIterator.next();
            Long phoneNumber = abonentPair.getKey();
            List<CallInformation> callInformationList = abonentPair.getValue();
            UUID tariffId = abonentTariffMap.get(phoneNumber);
            Abonent abonent = Abonent.builder()
                    .phoneNumber(phoneNumber)
                    .tariff(Tariff.builder().tariffId(tariffId).build())
                    .build();
            outputStream.write((this.abonentSerializer.apply(abonent) + "\n").getBytes());
            Iterator<CallInformation> callIterator = callInformationList.listIterator();
            while (callIterator.hasNext()) {
                CallInformation information = callIterator.next();
                outputStream.write(this.callInformationSerializer.apply(information).getBytes());
                if(callIterator.hasNext()) {
                    outputStream.write((byte) '\n');
                }
            }
            if(abonentIterator.hasNext()) {
                outputStream.write("\n\n".getBytes());
            }
        }
    }
}
