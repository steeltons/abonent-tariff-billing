package org.jenjetsu.com.hrs.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.hrs.logic.BillFileCreator;
import org.jenjetsu.com.hrs.logic.CdrPlusFileParser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
@Slf4j
public class BillController {

    private final BillFileCreator fileCreator;
    private final CdrPlusFileParser cdrPlusFileParser;

    /**
     * <h2>billCdrPlusFile</h2>
     * <p>Bill input cdr plus file</p>
     * @param file - cdr plus file
     * @return ResponseEntity<?> - 501 status without body
     * @deprecated will be implemented in another version in another method
     */
    @PostMapping("/billing")
    @Deprecated(forRemoval = true)
    public ResponseEntity<?> billCdrPlusFile(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
