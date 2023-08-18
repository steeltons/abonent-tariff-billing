package org.jenjetsu.com.brt.logic;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class CdrFileParser {

    /**
     * <h2>Parse cdr file to abonent calls</h2>
     * Parse input cdr file and group calls by caller
     * @param cdrFile
     * @return
     */
    public Map<Long, List<CallInformation>> parseCdrFileToAbonentCalls(Resource cdrFile){
        try(Scanner scanner = new Scanner(cdrFile.getInputStream())) {
            Map<Long, List<CallInformation>> abonentCallsMap = new HashMap<>();
            while (scanner.hasNext()) {
                CallInformation call = CallInformation.parseFromLine(scanner.nextLine());
                if(!abonentCallsMap.containsKey(call.getPhoneNumber())) {
                    abonentCallsMap.put(call.getPhoneNumber(), new ArrayList<>());
                }
                abonentCallsMap.get(call.getPhoneNumber()).add(call);
            }
            return abonentCallsMap;
        } catch (Exception e) {
            log.error("Error parsing cdr file {}.", cdrFile.getFilename());
            throw new RuntimeException("Impossible to parse cdr file.", e);
        }
    }

}
