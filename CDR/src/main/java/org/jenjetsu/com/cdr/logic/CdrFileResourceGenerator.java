package org.jenjetsu.com.cdr.logic;

import org.jenjetsu.com.core.entity.CallInformation;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

@Service
public class CdrFileResourceGenerator {

    /**
     * <h2>Create resource from calls</h2>
     * @param calls - collection of calls
     * @return resource - cdr byte addy resource
     */
    public Resource generateCdrResourceFromCalls(Collection<CallInformation> calls) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Iterator<CallInformation> iter = calls.iterator();
            while (iter.hasNext()) {
                String line = iter.next().toString() + ((iter.hasNext()) ? "\n" : "");
                out.write(line.getBytes(StandardCharsets.UTF_8));
            }
            out.close();
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray()) {
                private final String filename = UUID.randomUUID().toString() + ".cdr";
                @Override
                public String getFilename() {
                    return filename;
                }
            };
            return resource;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Impossible to create cdr file. Error message: %s", e.getMessage()));
        }
    }
}
