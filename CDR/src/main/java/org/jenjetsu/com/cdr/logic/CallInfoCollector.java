package org.jenjetsu.com.cdr.logic;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

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

    @PostConstruct
    public void init() {
        log.info("CallInfoCollector: start reading saved calls.");
        try {
            Path path = Path.of("calls");
            File[] callFiles = path.toFile().listFiles();
            for(File callFile : callFiles) {
                Scanner scanner = new Scanner(callFile);
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    CallInformation call = CallInformation.parseFromLine(line);
                    this.callInformationList.add(call);
                }
                scanner.close();
            }
            for(File callFile : callFiles) {
                callFile.delete();
            }
            log.info("CallInfoCollector: end reading saved calls");
        } catch (Exception e) {
            log.error("CallInfoCollector: impossible to read saved calls. Error message {}", e.getMessage());
        }
    }
    @PreDestroy
    public void destroy() {
        log.info("CallInfoCollector: start saving all calls");
        try {
            Instant saveCallsTime = Instant.now();
            Path callsFilePath = Path.of("calls/calls-" + saveCallsTime.toString()+".txt");
            Path callsDirPath = callsFilePath.getParent();
            if(callsDirPath != null && !Files.exists(callsDirPath)) {
                Files.createDirectory(callsDirPath);
            }
            PrintWriter writer = new PrintWriter(new FileWriter(callsFilePath.toFile()));
            Iterator<CallInformation> iter = callInformationList.listIterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iter.hasNext()) {
                CallInformation call = iter.next();
                stringBuilder.append(call.toString());
                if(iter.hasNext()) {
                    stringBuilder.append("\n");
                }
            }
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
            log.info("CallInfoCollector: end writing calls. File name {}", callsDirPath.getFileName());
        } catch (Exception e) {
            log.error("IMPOSSIBLE TO SAVE CALLS INTO FILE! ERROR MESSAGE: {}", e.getMessage());
            throw new Error(e);
        }
    }
}
