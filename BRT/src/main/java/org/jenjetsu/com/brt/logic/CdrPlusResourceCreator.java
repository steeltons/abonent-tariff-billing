package org.jenjetsu.com.brt.logic;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.brt.entity.CallOption;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.service.AbonentService;
import org.jenjetsu.com.brt.service.TariffService;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.format;
import static java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class CdrPlusResourceCreator {

    private final TariffService tariffService;
    private final AbonentService abonentService;
    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat minuteCostDecimalFormat = new DecimalFormat("00.00", formatSymbols);
    private final DecimalFormat optionCostDecimalFormat = new DecimalFormat("000.00", formatSymbols);
    private final DecimalFormat tariffCostDecimalFormat = new DecimalFormat("0000.00", formatSymbols);
    private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * convert abonent calls to binary file.
     * Output example:
     * tariff_id_1 base_cost
     * call_type minute_cost minute_buffer call_priority types_linked cost
     * ...................................................................
     * tariff_id_N base_cost
     * ...................................................................
     * END OF TARIFFS
     * phone_number_1 tariff_id
     * call_type call_to start_calling_time end_calling_time
     * call_type call_to start_calling_time end_calling_time
     * ...................................................................
     * phone_number_N tariff_id
     * ...................................................................
     * EOF
     * @param abonentCallMap
     * @return binary file
     */
    public Resource convertToCdrPlusFile(Map<Long, List<CallInformation>> abonentCallMap) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Map<Long, UUID> abonentTariffMap = new HashMap<>();
            List<Tariff> tariffList = abonentCallMap.keySet().stream()
                    .map((phoneNumber) -> {
                            Tariff tariff = this.tariffService.getTariffByAbonentPhoneNumber(phoneNumber);
                            abonentTariffMap.put(phoneNumber, tariff.getTariffId());
                            return tariff;
                        })
                    .distinct()
                    .toList();

            this.writeHeader(tariffList, outputStream);
            outputStream.write("END OF TARIFFS\n".getBytes());
            this.writeCalls(abonentCallMap, abonentTariffMap, outputStream);
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

    /**
     * Write all tariff to file
     * @param tariffList
     * @param outputStream
     */
    private void writeHeader(List<Tariff> tariffList, OutputStream outputStream) throws IOException {
        Iterator<Tariff> tariffIterator = tariffList.listIterator();
        StringBuilder builder = new StringBuilder();
        while (tariffIterator.hasNext()) {
            Tariff tariff = tariffIterator.next();
            String tariffFormattedString = this.convertTariffToString(tariff);
            builder.append(tariffFormattedString);
            builder.append("\n");
            List<String> optionFormattedStringList = tariff.getCallOptionList()
                    .stream()
                    .map(this::convertCallOptionToString)
                    .toList();
            Iterator<String> optionIter = optionFormattedStringList.listIterator();
            while (optionIter.hasNext()) {
                String formatted = optionIter.next();
                builder.append(formatted);
                if(optionIter.hasNext()) builder.append("\n"); // check is option last to avoid \n
            }
            if(tariffIterator.hasNext()) builder.append("\n");
        }
        outputStream.write(builder.toString().getBytes());
    }

    /**
     * Write phone calls to file
     * @param phoneCallMap
     * @param phoneTariffMap
     * @param outputStream
     */
    private void writeCalls(Map<Long, List<CallInformation>> phoneCallMap,
                            Map<Long, UUID>  phoneTariffMap,
                            OutputStream outputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        Iterator<Entry<Long, List<CallInformation>>> iter = phoneCallMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, List<CallInformation>> pair = iter.next();
            Long phoneNumber = pair.getKey();
            List<CallInformation> calls = pair.getValue();
            UUID tariffId = phoneTariffMap.get(phoneNumber);
            builder.append(phoneNumber + " " + tariffId);
            builder.append("\n");
            Iterator<CallInformation> callIter = calls.listIterator();
            while (callIter.hasNext()) {
                CallInformation call = callIter.next();
                builder.append(this.convertAbonentCallToString(call));
                if(callIter.hasNext()) builder.append("\n");
            }
            if(iter.hasNext()) builder.append("\n");
        }
        outputStream.write(builder.toString().getBytes());
    }


    /**
     * Convert Tariff to formatted string
     * Output Example:
     * "tariff_id base_cost"
     * Where:
     * # tariff_id - an id of tariff - write as string
     * # base_cost - tariff base cost - write as float
     * @param tariff
     * @return formatted string
     */
    private String convertTariffToString(Tariff tariff) {
        return format(
                        "%s %s",
                        tariff.getTariffId().toString(),
                        tariffCostDecimalFormat.format(tariff.getBaseCost())
                     );
    }

    /**
     * Convert call option to formatted string
     * Output example:
     * "call_type minute_cost minute_buffer call_priority types_linked cost"
     * Where:
     * # call_type - type of call: INPUT(0), OUTPUT(1) - write as byte
     * # minute_cost - cost per minute - write as float
     * # minute_buffer - count of minutes, that abonent can spend for minute price - write as short
     * # call_priority - priority of option, 0 - highest, 10 - lowest - write as byte
     * # types_linked - mean that INPUT and OUTPUT call use same minute buffer on same
     *   or nearest call priority - write as byte (1 or 0)
     * # cost - general option cost - write as float
     * @param option
     * @return formatted string
     */
    private String convertCallOptionToString(CallOption option) {
        return format(
                "%d %s %d %d %d %s",
                option.getCallType().getCode(),
                minuteCostDecimalFormat.format(option.getCallCost()),
                option.getCallBuffer(),
                option.getCallPriority(),
                option.getAreCallTypesLinked() ? 1 : 0,
                optionCostDecimalFormat.format(option.getCost())
        );
    }

    /**
     * Convert Abonent call to formatted string
     * Output example:
     * "call_type call_to start_calling_time end_calling_time"
     * Where:
     * # call_type - type of call, INPUT(0), OUTPUT(1) - write as byte
     * # call_to - number of listener - write as long
     * # start_calling_time - date and time when call started
     *   - write as format yyyyMMddHHmmSS
     * # end_calling_time - date and time when call ends
     *   - write as format yyyyMMddHHmmss
     * @param call
     * @return formatted string
     */
    private String convertAbonentCallToString(CallInformation call) {
       return format(
               "%d %d %s %s",
               call.getCallTypeCode(),
               call.getCallToNumber(),
               dateFormat.format(call.getStartCallingTime()),
               dateFormat.format(call.getEndCallingTime())
       );
    }
}
