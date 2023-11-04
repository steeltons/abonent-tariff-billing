package org.jenjetsu.com.brt.broker;

import org.jenjetsu.com.brt.billing.BillingProcess;
import org.jenjetsu.com.brt.exception.BillingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BillingExceptionListener {

    private final BillingProcess<?> billingProcess;

    public BillingExceptionListener(@Qualifier("standardBillingProcess") BillingProcess billingProcess) {
        this.billingProcess = billingProcess;
    }

    @RabbitListener(queues = "brt-billing-exception-queue")
    public void handleBillingException(String exceptionMessage) {
        BillingException exception = new BillingException(exceptionMessage);
        this.billingProcess.setThrownException(exception);
    }
}
