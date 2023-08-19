package org.jenjetsu.com.brt.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
@Scope("singleton")
public class BillingProcess {

    private volatile Object data;
    private final Long timeout;
    private volatile Date start;
    private TimeoutException timeoutException;
    private boolean transfer;

    public BillingProcess() {
        this(12000L);
    }

    public BillingProcess(Long timeout) {
        this.start = new Date(-2209024800000l); // Date is '1900-01-01 00:00:00'
        this.timeout = timeout;
        transfer = false;
    }

    public ResponseEntity<?> joinToBillingProcess() {
        tryUpdate();
        Thread thread = new Thread(this::receive);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("Billing process throw exception while was waiting. Error message: {}", e.getMessage());
        }
        try {
            Object data = this.getData();
            return ResponseEntity.ok(data);
        } catch (TimeoutException e) {
            log.info("Billing process stopped with status code 504. Maybe you need to check other modules?");
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Timeout on billing abonents");
        }
    }

    private synchronized void receive() {
        while (transfer) {
            if(timeout != 0 && start.before(new Date(System.currentTimeMillis() - timeout))) {
                timeoutException = new TimeoutException();
                Thread.currentThread().interrupt();
                return;
            }
            try {
                wait(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        Thread.currentThread().interrupt();
    }

    public synchronized void send(final Object data) {
        if(transfer) {
            transfer = false;
            this.data = data;
            notifyAll();
        }
    }

    public Object getData() throws TimeoutException {
        if(Objects.nonNull(timeoutException)) {
            throw timeoutException;
        }
        return data;
    }

    private boolean isExpired() {
        return data != null || timeoutException != null || start.before(new Date(System.currentTimeMillis() - timeout));
    }

    private synchronized void tryUpdate() {
        if(isExpired()) {
            this.data = null;
            this.start = new Date();
            this.timeoutException = null;
            this.transfer = true;
        }
    }
}
