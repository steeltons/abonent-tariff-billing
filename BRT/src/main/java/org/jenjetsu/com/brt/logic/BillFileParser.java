package org.jenjetsu.com.brt.logic;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.EOFException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class BillFileParser {

    public List<AbonentBill> parseBillFile(Resource billFile) {
        try(Scanner scanner = new Scanner(billFile.getInputStream())) {
            List<AbonentBill> abonentBillList = new ArrayList<>();
            while (scanner.hasNext()) {
                Long phoneNumber = parsePhoneNumber(scanner);
                List<CallBillInformation> callBills = parseCallBills(scanner);
                Double totalSum = parseTotalSum(scanner);
                abonentBillList.add(new AbonentBill(phoneNumber, callBills, totalSum));
            }
            return abonentBillList;
        } catch (Exception e) {
            log.error("Error parsing bill file {}.", billFile.getFilename());
            throw new RuntimeException("Error parsing bill file.", e);
        }
    }

    private Long parsePhoneNumber(Scanner scanner) throws EOFException, NumberFormatException{
        if(!scanner.hasNext()) throw new EOFException("Bill file EOF, but expected phone number");
        return Long.parseLong(scanner.nextLine());
    }

    private List<CallBillInformation> parseCallBills(Scanner scanner) throws ParseException, EOFException {
        List<CallBillInformation> callBillInformationList = new ArrayList<>();
        String scannerLine = scanner.nextLine();
        while (!scannerLine.startsWith("===")) {
            CallBillInformation billInformation = CallBillInformation.parseFromLine(scannerLine);
            callBillInformationList.add(billInformation);
            if(!scanner.hasNext()) {
                throw new EOFException("Bill file EOF, but there is no total sum.");
            }
            scannerLine = scanner.nextLine();
        }
        return callBillInformationList;
    }

    private Double parseTotalSum(Scanner scanner) throws EOFException{
        if(!scanner.hasNext()) {
            throw new EOFException("Bill file EOF, but there is no total sum.");
        }
        return Double.parseDouble(scanner.nextLine().split(" ")[2]);
    }
}
