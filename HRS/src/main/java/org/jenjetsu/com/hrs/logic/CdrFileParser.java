package org.jenjetsu.com.hrs.logic;

import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.entity.CallInformation;
import org.jenjetsu.com.hrs.entity.Tariff;
import org.jenjetsu.com.hrs.entity.TariffOption;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CdrFileParser {

    public static List<AbonentInformation> parseCallInformationFromFile(InputStream fileInputStream) {
        List<AbonentInformation> abonentInformations = new ArrayList<>();
        try(Scanner scanner = new Scanner(fileInputStream)) {
            while (scanner.hasNext()) {
                AbonentInformation abonentInformation = new AbonentInformation();
                abonentInformation.setPhoneNumber(Long.parseLong(scanner.nextLine()));
                abonentInformation.setTariff(parseTariffFromLine(scanner.nextLine()));
                List<TariffOption> tariffOptions = new ArrayList<>();
                String line = scanner.nextLine();
                while (line.startsWith("-")) {
                    tariffOptions.add(parseTariffOptionFromLine(line));
                    line = scanner.nextLine();
                }
                List<CallInformation> callInformations = new ArrayList<>();
                while (!line.startsWith("=")) {
                    callInformations.add(parseCallInformationFromLine(line));
                    line = scanner.nextLine();
                }
                abonentInformation.setTariffOptions(tariffOptions);
                abonentInformation.setCallInformations(callInformations);
                abonentInformations.add(abonentInformation);
            }
            return abonentInformations;
        } catch (Exception e) {
            throw new RuntimeException("Impossible to parse file");
        }
    }

    private static Tariff parseTariffFromLine(String line) {
        String[] words = line.split(" ");
        Tariff tariff = new Tariff();
        tariff.setTariffId(Integer.parseInt(words[0]));
        tariff.setBasePrice(Double.parseDouble(words[1]));
        tariff.setRegionCode(Long.parseLong(words[2]));
        return tariff;
    }

    private static TariffOption parseTariffOptionFromLine(String line) {
        String[] words = line.split(" ");
        TariffOption tariffOption = new TariffOption();
        tariffOption.setCallType(Byte.parseByte(words[1]));
        tariffOption.setMinuteCost(Double.parseDouble(words[2]));
        tariffOption.setOptionBuffer(Long.parseLong(words[3]));
        tariffOption.setOptionPrice(Double.parseDouble(words[4]));
        return tariffOption;
    }

    private static CallInformation parseCallInformationFromLine(String line) {
        try {
            String[] words = line.split(" ");
            CallInformation callInformation = new CallInformation();
            callInformation.setCallType(Byte.parseByte(words[0]));
            callInformation.setCallToNumber(Long.parseLong(words[1]));
            callInformation.setIsCallToSameProvider(Integer.parseInt(words[2]) != 0);
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            callInformation.setStartCallingTime(format.parse(words[3]));
            callInformation.setEndCallingTime(format.parse(words[4]));
            return callInformation;
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(String.format("Impossible to parse line. Error message: %s", e.getMessage()));
        } catch (ParseException e) {
            throw new RuntimeException(String.format("Impossible to parse date. Error message: %s", e.getMessage()));
        }
    }
}
