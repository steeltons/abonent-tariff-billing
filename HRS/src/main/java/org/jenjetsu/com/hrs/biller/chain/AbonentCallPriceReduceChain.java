package org.jenjetsu.com.hrs.biller.chain;

import org.jenjetsu.com.hrs.biller.AbstractBillChain;
import org.jenjetsu.com.hrs.biller.BillingException;
import org.jenjetsu.com.hrs.model.AbonentHrs;
import org.jenjetsu.com.hrs.model.AbonentHrsOut;
import org.jenjetsu.com.hrs.model.TariffHrs;

/**
 * <h2>AbonentTotalPriceChain</h2>
 * <p>Standart chain that sum all call cost, tariff base cost and options cost and save it into output abonent</p>
 */
public class AbonentCallPriceReduceChain extends AbstractBillChain {

    @Override
    protected void doBill(AbonentHrs input, AbonentHrsOut output) throws BillingException {
        TariffHrs tariff =input.getTariff();
        float totalPrice = output.getTariffedCallList().stream()
                .reduce(0f, (acc, call) -> acc + call.getCallCost(), Float::sum);
        totalPrice += tariff.getCallOptionCardList().stream()
                .reduce(tariff.getBaseCost(), (acc, card) -> acc + card.getCardCost(), Float::sum);
        output.setTotalPrice(totalPrice);
    }
}
