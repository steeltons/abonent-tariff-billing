package org.jenjetsu.com.hrs.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.jenjetsu.com.core.entity.ByteArrayResourceWrapper;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.jenjetsu.com.hrs.model.TariffedCall;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillFileCreator implements Function<List<AbonentHrsOut>, Resource> {

    private final Function<TariffedCall, String> callSerializer;
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
     * @return ByteArrayResourceWrapper - bill file
     */
    public Resource apply(List<AbonentHrsOut> abonentOutList) {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Iterator<AbonentHrsOut> abonentIterator = abonentOutList.listIterator();
            while (abonentIterator.hasNext()) {
                AbonentHrsOut abonent = abonentIterator.next();
                outputStream.write((this.abonentOutSerializer.apply(abonent) + "\n").getBytes());
                this.writeTariffedCalls(abonent.getTariffedCallList(), outputStream);
                if(abonentIterator.hasNext()) {
                    outputStream.write("\n\n".getBytes());
                }
            }
            return ByteArrayResourceWrapper.billResource(outputStream.toByteArray());
        } catch (Exception e) {
            String message = "Impossible to create bill file. Error message: " + e.getMessage();
            throw new RuntimeException(message);
        }
    }

    private void writeTariffedCalls(Collection<TariffedCall> tariffedCallList, 
                                    OutputStream outputStream) throws IOException{
        Iterator<TariffedCall> callIterator = tariffedCallList.iterator();
        while(callIterator.hasNext()) {
            TariffedCall call = callIterator.next();
            outputStream.write(this.callSerializer.apply(call).getBytes());
            if(callIterator.hasNext()) {
                outputStream.write((byte) '\n');
            }
        }
    }
}
