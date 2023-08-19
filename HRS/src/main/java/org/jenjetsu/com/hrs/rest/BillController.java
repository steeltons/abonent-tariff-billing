package org.jenjetsu.com.hrs.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.logic.BillFileCreator;
import org.jenjetsu.com.hrs.logic.CallBiller;
import org.jenjetsu.com.hrs.logic.CdrPlusFileParser;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class BillController {

    private final CallBiller callBiller;
    private final BillFileCreator fileCreator;
    private final CdrPlusFileParser cdrPlusFileParser;

    @PostMapping("/billing")
    public ResponseEntity<?> billCdrPlusFile(@RequestParam("file")MultipartFile file) {
        if(!file.getOriginalFilename().endsWith(".cdrplus")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not correct file");
        }
        try {
            List<AbonentInformation> list = cdrPlusFileParser.parseCdrPlusFile(new InputStreamResource(file.getInputStream()));
            List<AbonentBill> abonentBillList = new ArrayList<>();
            for(AbonentInformation abonentInformation : list) {
                abonentBillList.add(callBiller.billAbonent(abonentInformation));
            }
            Resource billFile = fileCreator.writeBillsToFile(abonentBillList);
            return ResponseEntity.ok(billFile);
        } catch (Exception e) {
            log.error("Error parsing cdr plus file in controller!");
            throw new RuntimeException(e);
        }
    }
}
