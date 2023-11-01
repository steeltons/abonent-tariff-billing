package org.jenjetsu.com.hrs.biller;

import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;

/**
 * <h2>AbstractBillChain</h2>
 * <p>Implementation of pattern <code>Chain of responsibility</code></p>
 * <p>Do some operations over input abonent and save them into output abonent</p>
 */
@Slf4j
public abstract class AbstractBillChain {

    AbstractBillChain next;

    /**
     * <h2>processBilling</h2>
     * <p>Entry point for biling abonents</p>
     * @param input - input Abonent with tariff and unbilled calls
     * @param output - output Abonent with billed calles
     */
    public final void processBilling(AbonentHrs input, AbonentHrsOut output) {
        try {
            this.doBill(input, output);
            if(next != null) {
                next.processBilling(input, output);
            }
        } catch (BillingException e) {
            log.error("Error in chain {}", this.getClass().getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * <h2>doBill</h2>
     * <p>Abstract method that make operations over abonent information</p>
     * @param input - input Abonent with tariff and unbilled calls
     * @param output - output Abonent with billed calles
     * @throws BillingException
     */
    protected abstract void doBill(AbonentHrs input, AbonentHrsOut output) throws BillingException;

    protected void setNext(AbstractBillChain next) {
        this.next = next;
    }

}
