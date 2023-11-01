package org.jenjetsu.com.brt.serializer;

import org.jenjetsu.com.brt.entity.CallOptionCard;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import static java.lang.String.format;

@Component
public class CallOptionCardSerializer implements Function<CallOptionCard, String> {

    private final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat cardCostFormat = new DecimalFormat("###.##", formatSymbols);

    /**
     * <h2>callOptionCardSerializer</h2>
     * <p>Convert CallOptionCard to formatted string</p>
     * <p>Output example:</p>
     * <code>"- tariff_id input_option_id output_option_id shared_buffer card_priority card_cost"</code>
     * <p>Where:</p>
     * <p><b># tariff_id</b> - id of tariff - write as uuid</p>
     * <p><b># input_option_id</b> - id of option - write as long</p>
     * <p><b># output_option_id</b> - id of option - write as long</p>
     * <p><b># shared_buffer</b> - amount of minutes that input and output share - write as short</p>
     * <p><b># card_priority</b> - priority of card - write as byte</p>
     * <p><b># card_cost</b> - cost of card - write as float</p>
     * @param c - input call option card
     * @return String
     */
    public String apply(CallOptionCard c) {
        Long inputCallOption = Objects.nonNull(c.getInputOption()) ? c.getInputOption().getCallOptionId() : null;
        Long outputCallOption = Objects.nonNull(c.getOutputOption()) ? c.getOutputOption().getCallOptionId() : null;
        return format("- %s %d %d %d %d %s",
                    c.getTariff().getTariffId().toString(),
                    inputCallOption,
                    outputCallOption,
                    c.getSharedMinuteBuffer(),
                    c.getCardPriority(),
                    cardCostFormat.format(c.getCardCost().floatValue())
                );
    }
}
