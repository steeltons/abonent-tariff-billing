package org.jenjetsu.com.brt.billing;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.broker.CdrMessageSender;
import org.jenjetsu.com.brt.dto.AbonentBillingResultDTO;
import org.jenjetsu.com.brt.exception.BillingException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.jenjetsu.com.brt.billing.BillingStatus.*;

@Service
@Slf4j
public class StandardBillingProcess implements BillingProcess<List<AbonentBillingResultDTO>> {

    private final Long STANDARD_SECONDS_WAIT;

    private volatile BillingStatus BILLING_STATUS;
    private volatile List<AbonentBillingResultDTO> currentData;
    private volatile Exception thrownException;

    private Instant lastBillingDate;
    private final Duration billingPause;
    private final CdrMessageSender messageSender;

    public StandardBillingProcess(CdrMessageSender sender) {
        this.lastBillingDate = null;
        this.billingPause = Duration.ofSeconds(300);
        this.messageSender = sender;
        this.BILLING_STATUS = FINISHED;
        this.thrownException = null;
        this.currentData = null;
        this.STANDARD_SECONDS_WAIT = 50l;
    }

    @Override
    public synchronized BillingStatus startBilling(String command) {
        if(BILLING_STATUS.equals(FINISHED) || this.isPauseExpired()) {
            this.messageSender.sendCdrCommand(command);
            this.BILLING_STATUS = CDR_PROCESSING;
            this.lastBillingDate = Instant.now();
            this.thrownException = null;
        }
        return BILLING_STATUS;
    }

    @Override
    public List<AbonentBillingResultDTO> getCurrentData() {
        return currentData;
    }

    @Override
    public List<AbonentBillingResultDTO> joinToProcess(long timeout) throws Exception {
        if(BILLING_STATUS.equals(FINISHED)) {
            return currentData;
        }
        if(thrownException != null) {
            throw thrownException;
        }
        Thread t = new Thread(() -> receive(timeout));
        t.start();
        t.join();
        if(thrownException != null) {
            throw thrownException;
        }
        if(!BILLING_STATUS.equals(FINISHED)) {
            throw new RuntimeException("Billing wait timeout! BRT is still billing");
        }
        return this.getCurrentData();
    }

    private synchronized void receive(long timeToWait) {
        try {
            if(timeToWait < 0) {
                wait();
            } else if (timeToWait == 0) {
                wait(this.STANDARD_SECONDS_WAIT * 1000l);
            } else {
                wait(timeToWait * 1000l);
            }
        } catch (InterruptedException e) {
            log.error("Something interrupt me. Error message: {}", e.getMessage());
            notifyAll();
        }
    }

    @Override
    public synchronized void setCurrentData(List<AbonentBillingResultDTO> newData) throws Exception {
        if(!BILLING_STATUS.equals(BRT_BILLING)) {
            throw new BillingException("Impossible to set new data when billing process is not BRT_BILLING");
        }
        if(this.thrownException != null) {
            throw new BillingException("Impossible to set new data when exception was thrown. Start new billing!");
        }
        this.currentData = newData;
        this.BILLING_STATUS = FINISHED;
        notifyAll();
    }

    @Override
    public BillingStatus getCurrentStatus() {
        return BILLING_STATUS;
    }

    @Override
    public synchronized void updateStatus(BillingStatus newStatus) {
        if(!BILLING_STATUS.equals(newStatus)) {
            BILLING_STATUS = newStatus;
        }
    }

    @Override
    public synchronized BillingProcessInformation getInformation() {
        long secondsToWait = 0;
        if(this.lastBillingDate != null) {
            Instant endPause = this.lastBillingDate.plus(this.billingPause);
            Instant currentTime = Instant.now();
            secondsToWait = endPause.getEpochSecond() - currentTime.getEpochSecond();
        }
        return BillingProcessInformation.builder()
                .currentBillingStatus(BILLING_STATUS)
                .lastExceptionMessage(thrownException != null ? thrownException.getMessage() : "nope")
                .lastStartBillingDate(this.lastBillingDate)
                .billingPauseTimeout(this.billingPause)
                .timeToWait(secondsToWait < 0 ? Duration.ZERO : Duration.ofSeconds(secondsToWait))
                .build();
    }

    @Override
    public synchronized void setThrownException(Exception cause) {
        thrownException = cause;
        log.error("Billing process ends with exception: {}" , cause.getMessage());
        notifyAll();
    }

    private boolean isPauseExpired() {
        return this.lastBillingDate == null || this.lastBillingDate.plus(this.billingPause).isBefore(Instant.now());
    }
}
