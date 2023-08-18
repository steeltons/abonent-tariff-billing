package org.jenjetsu.com.cdr.logic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CallInfoCollector {

    private final List<CallInformation> callInformationList = new ArrayList<>();
    private final CdrFileResourceGenerator resourceGenerator;

    public void addNewCalls(Collection<CallInformation> calls) {
        this.callInformationList.addAll(calls);
    }

    public void addCall(CallInformation call) {
        this.callInformationList.add(call);
    }

    public List<CallInformation> getAllCalls() {
        return List.copyOf(this.callInformationList);
    }

    public void clean() {
        this.callInformationList.clear();
    }

    /**
     * <h2>Create cdr file</h2>
     * Convert all call information in local list to cdr file
     * @return cdr file as resource
     */
    public Resource createCdrFile() {
        log.info("Start convert all exist phone calls of size <{}> to cdr file.", callInformationList.size());
        Resource cdrFile = resourceGenerator.generateCdrResourceFromCalls(callInformationList);
        callInformationList.clear();
        log.info("End convert cdr file.");
        return cdrFile;
    }
}
