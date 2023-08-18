package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.entity.TariffCallOption;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.brt.service.TariffService;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class CdrPlusResourceCreator {

    private final TariffService tariffService;
    private final AbonentService abonentService;

    public Resource convertToCdrPlusFile(Map<Long, List<CallInformation>> abonentCallMap) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Iterator<Map.Entry<Long, List<CallInformation>>> mapIter = abonentCallMap.entrySet().iterator();
            while (mapIter.hasNext()){
                Map.Entry<Long, List<CallInformation>> abonentCalls = mapIter.next();
                Long phoneNumber = abonentCalls.getKey();
                List<CallInformation> calls = abonentCalls.getValue();
                Tariff tariff = tariffService.getTariffByAbonentPhoneNumber(phoneNumber);
                outputStream.write(writeHeader(phoneNumber, tariff).getBytes());
                outputStream.write(writeTariffOptions(tariff.getCallOptionList()).getBytes());
                outputStream.write((writeAbonentCalls(calls) + (mapIter.hasNext() ? "\n" : "")).getBytes());
            }
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

    private String writeHeader(Long phoneNumber, Tariff tariff) {
        String line = "";
        line += phoneNumber + "\n";
        line += String.format("%d %f %d\n", tariff.getTariffCode(), tariff.getBaseCost(), tariff.getRegion().getRegionId());
        return line;
    }

    private String writeTariffOptions(List<TariffCallOption> tariffCallOptionList) {
        StringBuilder line = new StringBuilder();
        tariffCallOptionList.sort((tco1, tco2) -> Byte.compare(tco2.getCallPriority(), tco1.getCallPriority()));
        for(TariffCallOption callOption : tariffCallOptionList) {
            line.append(String.format("- %d %f %d %f\n", callOption.getCallType(), callOption.getMinuteCost(),
                    callOption.getMinuteBuffer(), callOption.getOptionPrice()));
        }
        line.append("=============================\n");
        return line.toString();
    }

    private String writeAbonentCalls(List<CallInformation> callInformationList) {
        StringBuilder line = new StringBuilder();
        for (CallInformation call : callInformationList) {
            byte callType = call.getCallTypeCode();
            long callTo = call.getCallToNumber();
            int isSame = abonentService.isExistByPhoneNumber(call.getPhoneNumber()) ? 1 : 0;
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String startCallingDate = format.format(call.getStartCallingTime());
            String endCallingDate = format.format(call.getEndCallingTime());
            line.append(String.format(
                    "%d %d %d %s %s\n",
                    callType, callTo, isSame, startCallingDate, endCallingDate));
        }
        line.append("=============================");
        return line.toString();
    }
}
