package org.jenjetsu.com.hrs.biller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jenjetsu.com.hrs.biller.chain.AbonentCallBillerChain;
import org.jenjetsu.com.hrs.biller.chain.AbonentCallPriceReduceChain;
import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.jenjetsu.com.hrs.model.implementation.AbonentHrsOutImpl;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
public class BillingMachine {

    private final Log logger = LogFactory.getLog(BillingMachine.class);
    private boolean debug;

    private AbstractBillChain head;

    public BillingMachine() {
        this.head = new AbonentCallBillerChain();
        this.head.setNext(new AbonentCallPriceReduceChain());
        this.debug = false;
    }

    /**
     * <h2>startBilling</h2>
     * <p>Start billing one abonent through all registered billing chains</p>
     * @param inputAbonent - input Abonent with tariff and unbilled calls
     * @return AbonentHrsOut
     */
    public AbonentHrsOut startBilling(AbonentHrs inputAbonent) {
        if(debug) {
            logger.info(this.getBillingChainsInString());
        }
        AbonentHrsOut outputAbonent = AbonentHrsOutImpl.builder()
                .phoneNumber(inputAbonent.getPhoneNumber()).build();
        head.processBilling(inputAbonent, outputAbonent);
        return outputAbonent;
    }

    /**
     * <h2>startBlilling</h2>
     * <p>Start billing all abonents through all registered billing chains</p>
     * @param inputAbonentList - list of input Abonent with tariff and unbilled calls
     * @return List<AbonentHrsOut>
     */
    public List<AbonentHrsOut> startBilling(List<AbonentHrs> inputAbonentList) {
        if(debug) {
            logger.info(this.getBillingChainsInString());
        }
        List<AbonentHrsOut> list = new ArrayList<>();
        for(AbonentHrs inputAbonent : inputAbonentList) {
            AbonentHrsOut outputAbonent = AbonentHrsOutImpl.builder()
                    .phoneNumber(inputAbonent.getPhoneNumber()).build();
            head.processBilling(inputAbonent, outputAbonent);
            list.add(outputAbonent);
        }
        return list;
    }

    /**
     * <h2>addChain</h2>
     * <p>Add unique bill chain into chain queue</p>
     * @param chain - new bill chain
     * @return BillingMachine - self return
     * @exception IllegalArgumentException - if chain contains in queue
     */
    public BillingMachine addChain(AbstractBillChain chain) {
        if(head == null) {
            this.head = chain;
            return this;
        }
        AbstractBillChain currentChain = head;
        this.checkDuplicates(chain.getClass());
        while (currentChain.next != null) {
            currentChain = currentChain.next;
        }
        currentChain.setNext(chain);
        return this;
    }

    /**
     * <h2>addChainBefore</h2>
     * <p>Add unique bill chain into chain queue before another chain</p>
     * @param chain - new bill chain
     * @param beforeClass - chain to insert before
     * @return BillingMachine - self return
     * @exception IllegalArgumentException - if chain contains in queue or not contains before chain
     */
    public BillingMachine addChainBefore(AbstractBillChain chain, Class<? extends AbstractBillChain> beforeClass) {
        AbstractBillChain currentChain = head;
        this.checkDuplicates(chain.getClass());
        while (currentChain.next != null && !currentChain.next.getClass().equals(beforeClass)) {
            currentChain = currentChain.next;
        }
        if(currentChain.next == null) {
            throw new IllegalArgumentException(format("No such chain %s in chains", beforeClass.getName()));
        }
        AbstractBillChain beforeChain = currentChain.next;
        currentChain.setNext(chain);
        chain.setNext(beforeChain);
        return this;
    }

    /**
     * <h2>addChainAfter</h2>
     * <p>Add unique bill chain into chain queue after another chain</p>
     * @param chain - new bill chain
     * @param afterClass - chain to insert after
     * @return BillingMachine - self return
     * @exception IllegalArgumentException - if chain contains in queue or not contains after chain
     */
    public BillingMachine setChainAfter(AbstractBillChain chain, Class<? extends AbstractBillChain> afterClass) {
        AbstractBillChain currentChain = head;
        this.checkDuplicates(chain.getClass());
        while (currentChain != null && !currentChain.getClass().equals(afterClass)) {
            currentChain = currentChain.next;
        }
        if(currentChain == null) {
            throw new IllegalArgumentException(format("No such chain %s in chains", afterClass.getName()));
        }
        AbstractBillChain nextChain = currentChain.next;
        currentChain.setNext(chain);
        chain.setNext(nextChain);
        return this;
    }

    /**
     * <h2>removeChain</h2>
     * <p>Remove bill chain from queue</p>
     * @param chainClass - removable chain class
     * @return BillingMachine - self return
     * @exception IllegalArgumentException - if not contains removable chain
     */
    public BillingMachine removeChain(Class<? extends AbstractBillChain> chainClass) {
        AbstractBillChain currentChain = head;
        while (currentChain.next != null && !currentChain.next.getClass().equals(chainClass)) {
            currentChain = currentChain.next;
        }
        if(currentChain.next == null) {
            throw new IllegalArgumentException(format("No such chain %s in chains", chainClass.getName()));
        }
        AbstractBillChain removableChain = currentChain.next;
        currentChain.setNext(currentChain.next.next);
        removableChain.setNext(null);
        return this;
    }

    /**
     * <h2>debug</h2>
     * <p>Enable or disable debug mode.</p>
     * <p>Will log current filter chains, that'll be used for one Abonent or list.</p>
     * @param debug
     */
    public void debug(boolean debug) {
        this.debug = debug;
    }

    private void checkDuplicates(Class<? extends  AbstractBillChain> chainClass) {
        AbstractBillChain currentChain = head;
        while(currentChain != null && !currentChain.getClass().equals(chainClass)) {
            currentChain = currentChain.next;
        }
        if(currentChain != null) {
            throw new IllegalArgumentException(format("Billing chain is already contains chain %s", chainClass.getName()));
        }
    }

    private String getBillingChainsInString() {
        StringBuilder builder = new StringBuilder();
        AbstractBillChain currentChain = head;
        int counter = 0;
        while (currentChain != null) {
            builder.append(counter);
            builder.append(". ");
            builder.append(currentChain.getClass().getName());
            builder.append('\n');
            currentChain = currentChain.next;
        }
        return builder.toString();
    }
}
