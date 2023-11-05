package org.jenjetsu.com.brt.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.time.Duration;
import java.time.Instant;

@Builder
public record BillingProcessInformation(
            @JsonProperty("current_billing_status") BillingStatus currentBillingStatus,
            @JsonProperty("last_exception_message") String lastExceptionMessage,
            @JsonSerialize(using = BillingInstantSerializer.class)
            @JsonProperty("last_start_billing_date") Instant lastStartBillingDate,
            @JsonSerialize(using = BillingDurationSerializer.class)
            @JsonProperty("billing_pause_timeout") Duration billingPauseTimeout,
            @JsonSerialize(using = BillingDurationSerializer.class)
            @JsonProperty("time_to_wait") Duration timeToWait
        ) {
}
