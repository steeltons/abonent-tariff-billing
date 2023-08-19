package org.jenjetsu.com.hrs.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.entity.HrsCallInformation;
import org.jenjetsu.com.hrs.entity.Tariff;
import org.jenjetsu.com.hrs.entity.TariffOption;
import org.simpleframework.xml.util.ConcurrentCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.EOFException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class CdrPlusFileParser {

    private final CacheManager cacheManager;

    public List<AbonentInformation> parseCdrPlusFile(Resource cdrPlusFile) {
        List<AbonentInformation> abonentInformationList = new ArrayList<>();
        cacheManager.getCache("tariff-cache").clear();
        try(Scanner scanner = new Scanner(cdrPlusFile.getInputStream())) {
            while (scanner.hasNext()) {
                AbonentInformation abonentInformation = new AbonentInformation();
                abonentInformation.setPhoneNumber(parsePhoneNumber(scanner));
                abonentInformation.setTariff(parseTariff(scanner));
                abonentInformation.setCallInformations(parseCallInformations(scanner));
                abonentInformationList.add(abonentInformation);
            }
            return abonentInformationList;
        } catch (Exception e) {
            log.error("Impossible to parse cdr plus {} file.", cdrPlusFile.getFilename());
            throw new RuntimeException("Impossible to parse file", e);
        }
    }

    private Long parsePhoneNumber(Scanner scanner) throws EOFException {
        if(!scanner.hasNext()) {
            throw new EOFException("Cdr plus file EOF, but expected phone number.");
        }
        return Long.parseLong(scanner.nextLine());
    }

    private Tariff parseTariff(Scanner scanner) throws EOFException{
        if(!scanner.hasNext()) {
            throw new EOFException("Cdr plus file EOF, but expected tariff.");
        }
        Cache tariffCahce = cacheManager.getCache("tariff-cache");
        String[] words = scanner.nextLine().split(" ");
        Integer tariffId = Integer.parseInt(words[0]);
        Tariff tariff = null;
        if(tariffCahce != null && tariffCahce.get(tariffId) != null) {
            tariff = tariffCahce.get(tariffId, Tariff.class);
            while (!scanner.nextLine().startsWith("===")) {}
        } else {
            tariff = new Tariff();
            tariff.setTariffId(tariffId);
            tariff.setBasePrice(Double.parseDouble(words[1]));
            tariff.setRegionCode(Long.parseLong(words[2]));
            tariff.setTariffOptionList(parseTariffOptions(scanner));
            if(tariffCahce != null) {
                tariffCahce.put(tariffId, tariff);
            }
        }
        return tariff;
    }

    private List<TariffOption> parseTariffOptions(Scanner scanner) throws EOFException{
        if(!scanner.hasNext()) {
            throw new EOFException("Cdr plus file EOF, but expected minimum one incoming and one outcoming call options");
        }
        String scannerLine = scanner.nextLine();
        List<TariffOption> tariffOptions = new ArrayList<>();
        while (!scannerLine.startsWith("===")) {
            String[] words = scannerLine.split(" ");
            TariffOption tariffOption = new TariffOption();
            tariffOption.setCallType(Byte.parseByte(words[1]));
            tariffOption.setMinuteCost(Double.parseDouble(words[2]));
            tariffOption.setOptionBuffer(Long.parseLong(words[3]));
            tariffOption.setOptionPrice(Double.parseDouble(words[4]));
            tariffOptions.add(tariffOption);
            if(!scanner.hasNext()) {
                throw new EOFException("Cdr plus file EOF, but there is no ======= end. Impossible to understand where is end of options");
            }
            scannerLine = scanner.nextLine();
        }
        return tariffOptions;
    }

    private List<HrsCallInformation> parseCallInformations(Scanner scanner) throws EOFException, ParseException{
        if(!scanner.hasNext()) {
            throw new EOFException("Cdr plus file EOF, but expected call information or =====.");
        }
        String scannerLine = scanner.nextLine();
        List<HrsCallInformation> callList = new ArrayList<>();
        while (!scannerLine.startsWith("===") && scanner.hasNext()) {
            String[] words = scannerLine.split(" ");
            HrsCallInformation call = new HrsCallInformation();
            call.setCallType(Byte.parseByte(words[0]));
            call.setCallTo(Long.parseLong(words[1]));
            call.setIsSameOperator(Byte.parseByte(words[2]) == 1);
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            call.setStartCallingTime(format.parse(words[3]));
            call.setEndCallingTime(format.parse(words[4]));
            callList.add(call);
            scannerLine = scanner.nextLine();
        }
        return callList;
    }
}
