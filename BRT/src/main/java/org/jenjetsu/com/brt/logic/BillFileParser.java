package org.jenjetsu.com.brt.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

@Service
@Slf4j
public class BillFileParser implements Function<Resource, List<AbonentBill>> {

    private final Function<String, AbonentBill> abonentBillDeserializer;
    private final Function<String, CallBillInformation> callBillDeserializer;

    public BillFileParser(
            @Qualifier("abonentBillDeserializer") Function<String, AbonentBill> abonentBillDeserializer,
            @Qualifier("callBillInformationDeserializer") Function<String, CallBillInformation> callBillDeserializer) {
        this.abonentBillDeserializer = abonentBillDeserializer;
        this.callBillDeserializer = callBillDeserializer;
    }

    /**
     * <h2>parseBillFile</h2>
     * <p>Parse input bill file into billed abonents</p>
     * @param billFile
     * @return List<AbonentBill>
     */
    public List<AbonentBill> apply(Resource billFile) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(billFile.getInputStream()))) {
            String line;
            List<AbonentBill> abonentBillList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                AbonentBill abonentBill = this.abonentBillDeserializer.apply(line);
                List<CallBillInformation> callBillList = new ArrayList<>();
                while ((line = reader.readLine()) != null && line.startsWith("-")) {
                    CallBillInformation callBill = this.callBillDeserializer.apply(line);
                    callBillList.add(callBill);
                }
                abonentBill.setCallBillInformationList(callBillList);
                abonentBillList.add(abonentBill);
            }
            return abonentBillList;
        } catch (Exception e) {
            throw new RuntimeException("Impossible to parse bil file");
        }
    }
}
