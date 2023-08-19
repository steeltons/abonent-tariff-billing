package org.jenjetsu.com.hrs.logic;

import org.apache.logging.log4j.core.util.datetime.DateParser;
import org.jenjetsu.com.core.entity.AbonentBill;
import org.jenjetsu.com.core.entity.CallBillInformation;
import org.jenjetsu.com.hrs.entity.AbonentInformation;
import org.jenjetsu.com.hrs.entity.HrsCallInformation;
import org.jenjetsu.com.hrs.entity.TariffOption;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class CallBillerImpl implements CallBiller{

    private Duration incomingCallBuffer;
    private Duration outcomingCallBuffer;
    private Queue<TariffOption> incomingCallOptionQueue;
    private Queue<TariffOption> outcomingCallOptionQueue;
    private TariffOption currentIncomingCallOption;
    private TariffOption currentOutcomingCallOption;

    @Override
    public AbonentBill billAbonent(AbonentInformation abonentInformation) {
        init(abonentInformation);
        AbonentBill abonentBill = new AbonentBill();
        double totalSum = 0;
        List<CallBillInformation> callBillInformationList = new ArrayList<>();
        for(HrsCallInformation call : abonentInformation.getCallInformations())  {
            long callSeconds = call.getCallDurationInSeconds();
            double callCost = 0.0;
            if(call.getCallType() == 1) {
                while(incomingCallBuffer != null && incomingCallBuffer.getSeconds() - callSeconds <= 0) {
                    long callBufferMinutes = (long) Math.ceil(incomingCallBuffer.getSeconds() / 60.0);
                    callCost += callBufferMinutes * currentIncomingCallOption.getMinuteCost();
                    callSeconds -= incomingCallBuffer.getSeconds();
                    changeCurrentIncomingCallOption();
                }
                callCost += Math.ceil(callSeconds / 60.0) * currentIncomingCallOption.getMinuteCost();
                if(incomingCallBuffer != null) {
                    incomingCallBuffer = incomingCallBuffer.minusSeconds(callSeconds);
                }
            } else {
                while(outcomingCallBuffer != null && outcomingCallBuffer.getSeconds() - callSeconds <= 0) {
                    long callBufferMinutes = (long) Math.ceil(outcomingCallBuffer.getSeconds() / 60.0);
                    callCost += callBufferMinutes * currentOutcomingCallOption.getMinuteCost();
                    callSeconds -= outcomingCallBuffer.getSeconds();
                    changeCurrentOutcomingCallOption();
                }
                callCost += Math.ceil(callSeconds / 60.0) * currentOutcomingCallOption.getMinuteCost();
                if(outcomingCallBuffer != null) {
                    outcomingCallBuffer = outcomingCallBuffer.minusSeconds(callSeconds);
                }
            }
            callBillInformationList.add(new CallBillInformation(call.getCallType(), call.getCallTo(), call.getStartCallingTime(), call.getEndCallingTime(), call.getCallDurationInSeconds(), callCost));
            totalSum += callCost;
        }
        abonentBill.setPhoneNumber(abonentInformation.getPhoneNumber());
        abonentBill.setCallBillInformationList(callBillInformationList);
        totalSum += abonentInformation.getTariff().getBasePrice();
        abonentBill.setTotalCost(totalSum);
        return abonentBill;
    }

    private void init(AbonentInformation abonentInformation) {
        incomingCallOptionQueue = abonentInformation.getTariff().getTariffOptionList().stream().filter((to) -> to.getCallType() == 1).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        outcomingCallOptionQueue = abonentInformation.getTariff().getTariffOptionList().stream().filter((to) -> to.getCallType() == 2).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        changeCurrentIncomingCallOption();
        changeCurrentOutcomingCallOption();
    }

    private void changeCurrentIncomingCallOption() {
        currentIncomingCallOption = incomingCallOptionQueue.poll();
        incomingCallBuffer = currentIncomingCallOption.getOptionBuffer() > 0 ? Duration.ofMinutes(currentIncomingCallOption.getOptionBuffer()) : null;
    }

    private void changeCurrentOutcomingCallOption() {
        currentOutcomingCallOption = outcomingCallOptionQueue.poll();
        outcomingCallBuffer = currentOutcomingCallOption.getOptionBuffer() > 0 ? Duration.ofMinutes(currentOutcomingCallOption.getOptionBuffer()) : null;
    }
}
