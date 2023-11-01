package org.jenjetsu.com.hrs.biller.chain;

import lombok.Getter;
import lombok.Setter;
import org.jenjetsu.com.hrs.biller.AbstractBillChain;
import org.jenjetsu.com.hrs.biller.BillingException;
import org.jenjetsu.com.hrs.model.*;
import org.jenjetsu.com.hrs.model.implementation.TariffedCallImpl;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * <h2>AbonentCallBillerChain</h2>
 * <p>Standard chain that bill all abonents call numbers, that riches this chain</p>
 */
public class AbonentCallBillerChain extends AbstractBillChain {

    /**
     * <h2>billAbonentCalls</h2>
     * <p>Bill all abonent calls and store them into output abonent</p>
     * @param input - input Abonent with tariff and unbilled calls
     * @param output - output Abonent with billed calles
     * @throws BillingException
     */
    @Override
    public void doBill(AbonentHrs input, AbonentHrsOut output) throws BillingException {
        TariffHrs abonentTariff = input.getTariff();
        // Sort and clone all tariff call option cards for billing
        List<CallOptionCardHrs> clonedCards = abonentTariff.getCallOptionCardList().stream()
                .map(CallOptionCardHrs::clone)
                .toList();
        List<CallOptionHrs> inputOptionList = this.getInputOptionIterator(clonedCards);
        List<CallOptionHrs> outputOptionList = this.getOutputOptionIterator(clonedCards);
        output.setTariffedCallList(new ArrayList<>());

        for (CallHrs call : input.getCallList()) {
            List<CallOptionHrs> currentOptionList = switch (call.getCallType()) {
                case 0 -> outputOptionList;
                case 1 -> inputOptionList;
                default -> throw new IllegalArgumentException(String.format("No such call type as %d", call.getCallType()));
            };
            Wrapper<Long> callDurationWrapper = new Wrapper<>(call.getCallDurationInSeconds());
            float callCost = 0f;
            callCost += this.billFirstCallPart(currentOptionList, callDurationWrapper);
            callCost += this.billSecondCallPart(currentOptionList.get(0), callDurationWrapper.get());
            TariffedCall tariffedCall = TariffedCallImpl.builder()
                    .callType(call.getCallType())
                    .callTo(call.getCallTo())
                    .startCallingTime(call.getStartCallingTime())
                    .endCallingTime(call.getEndCallingTime())
                    .callDuration(Duration.ofSeconds(call.getCallDurationInSeconds()))
                    .callCost(callCost)
                    .build();
            output.getTariffedCallList().add(tariffedCall);
        }
    }

    /**
     * <p>Bill call price through all call options which buffers lower than call duration</p>
     * @param optionsList input or output call option
     * @param callDurationWrapper wrapper for call duration
     * @return Float - call price through buffers
     */
    private Float billFirstCallPart(List<CallOptionHrs> optionsList,
                                         Wrapper<Long> callDurationWrapper) {
        float particalCallCost = 0f;
        while (optionsList.get(0).getMinuteBuffer().getSeconds() < callDurationWrapper.get()) {
            CallOptionHrs currentOption = optionsList.get(0);
            long restSeconds = currentOption.getMinuteBuffer().getSeconds();
            long restMinutes = (long) Math.ceil(restSeconds / 60.0);
            particalCallCost += restMinutes * currentOption.getMinuteCost();
            callDurationWrapper.set(callDurationWrapper.get() - restSeconds);
            currentOption.getMinuteBuffer().clearToZero();
            optionsList.remove(0);
        }
        return particalCallCost;
    }

    /**
     * <p>Bill rest of call duration seconds that less than current call option buffer</p>
     * @param currentOption input or output option
     * @param restCallSeconds
     * @return Float - rest of call price
     */
    public Float billSecondCallPart(CallOptionHrs currentOption, Long restCallSeconds) {
        Long restCallMinutes = (long) Math.ceil(restCallSeconds / 60.0);
        currentOption.getMinuteBuffer().minusSeconds(restCallSeconds);
        return currentOption.getMinuteCost() * restCallMinutes;
    }

    private List<CallOptionHrs> getInputOptionIterator(List<CallOptionCardHrs> cardList) {
        return cardList.stream()
                .map(CallOptionCardHrs::getInputOption)
                .filter(Objects::nonNull)
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    private List<CallOptionHrs> getOutputOptionIterator(List<CallOptionCardHrs> cardList) {
        return cardList.stream()
                .map(CallOptionCardHrs::getOutputOption)
                .filter(Objects::nonNull)
                .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    // Like pointer in C++ for changing value between methods
    private class Wrapper<V> {
        private V value;

        public Wrapper(V value) {
            this.value = value;
        }

        public void set(V value) {
            this.value = value;
        }

        public V get() {
            return this.value;
        }
    }
}
