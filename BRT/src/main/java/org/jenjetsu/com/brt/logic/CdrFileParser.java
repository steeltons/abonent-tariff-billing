package org.jenjetsu.com.brt.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class CdrFileParser implements Function<Resource, Map<Long, List<CallInformation>>> {

    private final Function<String, CallInformation> callInformationDeserializer;

    /**
     * <h2>Parse cdr file to abonent calls</h2>
     * <p>Parse input cdr file and group calls by caller</p>
     * <p>Output example:</p>
     * <p>{<br>
     * &emsp;79147869165 : [{callType= 1, phoneNumber= 79147869165, callTo= 79000000000,<br>
     * &emsp;&emsp;         startCallingDate=20231212154300, endCallingDate=20231212154340}, {...}],<br>
     * &emsp;79147869166 : {...}<br>
     * }</p>
     * @param cdrFile
     * @return Map<Long, List<CallInformation>>
     */
    public Map<Long, List<CallInformation>> apply(Resource cdrFile){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(cdrFile.getInputStream()))) {
            Map<Long, List<CallInformation>> abonentCallsMap = new HashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                CallInformation call = this.callInformationDeserializer.apply(line);
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
