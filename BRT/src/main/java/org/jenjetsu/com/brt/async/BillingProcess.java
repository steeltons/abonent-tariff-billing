package org.jenjetsu.com.brt.async;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.broker.CdrMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * <h2>BillingProcess</h2>
 * <p>Control all process when BRT, CDR and HRS billing abonents.</p>
 * @deprecated Explained in joinToProcess and getData. Better use org.jenjetsu.com.brt.billing.StandardBillingProcess
 */
@Component
@Slf4j
@Scope("singleton")
@Deprecated(forRemoval = true)
public class BillingProcess {

    private volatile Object data;
    private final Long timeout;
    private volatile Date start;
    private TimeoutException timeoutException;
    private boolean transfer;
    private CdrMessageSender messageSender;

    public BillingProcess() {
        this(12000L);
    }

    public BillingProcess(Long timeout) {
        this.start = new Date(-2209024800000l); // Date is '1900-01-01 00:00:00'
        this.timeout = timeout;
        transfer = false;
    }

    /**
     * <h2>joinToBillingProcess</h2>
     * <p>Starts billing process, if not started, and subscribe threads to wait ends of billing for 50 seconds</p>
     * @param command billing command like "generate" or "collect-and-send"
     * @return ResponseEntity<?> - 200 with billing data or 5xx code
     * @deprecated Force subscribe thread to the process even he just wants to start billing without waiting.
     * Split to: joinToProcess, startBilling in org.jenjetsu.com.brt.billing.StandardBillingProcess
     */
    @Deprecated(forRemoval = true)
    public ResponseEntity<?> joinToBillingProcess(String command) {
        tryUpdate(command);
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

    /**
     * <h2>send</h2>
     * <p>Send result of billing</p>
     * @param data billing result
     * @deprecated Better use org.jenjetsu.com.brt.billing.StandardBillingProcess.setCurrentData(T data)
     */
    @Deprecated(forRemoval = true)
    public synchronized void send(final Object data) {
        if(transfer) {
            transfer = false;
            this.data = data;
            notifyAll();
        }
    }

    /**
     * <h2>getData</h2>
     * <p>Return result of billing or throw exception if billing process ends without result</p>
     * @return Object - billing result
     * @throws TimeoutException
     * @deprecated Impossible to get previous billing result, if current process ends with exception.
     * Better use org.jenjetsu.com.brt.billing.StandardBillingProcess.getCurrentData()
     */
    @Deprecated(forRemoval = true)
    public Object getData() throws TimeoutException {
        if(Objects.nonNull(timeoutException)) {
            throw timeoutException;
        }
        return data;
    }

    private boolean isExpired() {
        return data != null || timeoutException != null || start.before(new Date(System.currentTimeMillis() - timeout));
    }

    private synchronized void tryUpdate(String command) {
        if(isExpired()) {
            this.data = null;
            this.start = new Date();
            this.timeoutException = null;
            this.transfer = true;
            messageSender.sendCdrCommand(command);
        }
    }

    @Autowired
    @Deprecated(forRemoval = true)
    public void setMessageSender(CdrMessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
