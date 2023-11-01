package org.jenjetsu.com.hrs.logic;

import lombok.RequiredArgsConstructor;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.jenjetsu.com.hrs.model.TariffedCall;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BillFileCreator implements Function<List<AbonentHrsOut>, Resource> {

    private final Function<TariffedCall, String> tariffedCallSerializer;
    private final Function<AbonentHrsOut, String> abonentOutSerializer;

    /**
     * <h2>createBillFile</h2>
     * <p>Create bill file from billed abonents</p>
     * <p>Output example:</p>
     * <code>phone_number_1 total_price<br>
     * - call_type call_to start_calling_time end_calling_time call_duration call_cost<br>
     * .............................................................................<br>
     * - call_type call_to start_calling_time end_calling_time call_duration call_cost<br><br>
     * .............................................................................<br><br>
     * phone_number_N total_price<br>
     * .............................................................................</code>
     * @param abonentOutList billed abonents list
     * @return Resource - bill file
     */
    public Resource apply(List<AbonentHrsOut> abonentOutList) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Iterator<AbonentHrsOut> abonentIterator = abonentOutList.listIterator();
            while (abonentIterator.hasNext()) {
                AbonentHrsOut abonent = abonentIterator.next();
                outputStream.write((this.abonentOutSerializer.apply(abonent) + "\n").getBytes());
                Iterator<TariffedCall> callIterator = abonent.getTariffedCallList().iterator();
                while (callIterator.hasNext()) {
                    TariffedCall call = callIterator.next();
                    outputStream.write(this.tariffedCallSerializer.apply(call).getBytes());
                    if(callIterator.hasNext()) {
                        outputStream.write((byte) '\n');
                    }
                }
                if(abonentIterator.hasNext()) {
                    outputStream.write("\n\n".getBytes());
                }
            }
            return new ByteArrayResource(outputStream.toByteArray()) {
                private final String filename = UUID.randomUUID().toString()+".bill";
                @Override
                public String getFilename() {
                    return filename;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

}
