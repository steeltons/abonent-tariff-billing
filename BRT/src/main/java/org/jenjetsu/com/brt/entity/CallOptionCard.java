package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

/**
 * # sharedMinuteBuffer - amount of minutes that input and output calls share between themselves.
 *   Has some limitation:
 *   1. Zero value for not zero option priority means that call types not linked, they'll use individual buffers.
 *   2. Zero value for zero option is default, because zero option has unlimited time. Buffers are not linked
 */
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "call_option_card")
@Table(uniqueConstraints = {
                            @UniqueConstraint(columnNames = {"tariff_id", "input_option_id", "output_option_id"}),
                            @UniqueConstraint(columnNames = {"tariff_id", "option_priority"})
                            })
@Check(constraints = "(option_priority != 0 AND shared_minute_buffer != 0) || (option_priority = 0 AND shared_minute_buffer = 0)")
public class CallOptionCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID callOptionCardId;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "input_option_id", nullable = true)
    @Check(constraints = "input_option != null && output_option != null")
    private CallOption inputOption;
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "output_option_id", nullable = true)
    @Check(constraints = "input_option != null && output_option != null")
    private CallOption outputOption;
    @Column(name = "shared_minute_buffer", nullable = false, columnDefinition = "INT2 DEFAULT 0")
    @Min(0) @Max(1200)
    private Short sharedMinuteBuffer;
    @Column(name = "card_priority", nullable = false)
    @Min(0) @Max(10)
    private Byte cardPriority;
    @Column(name = "card_cost", nullable = false, scale = 4, length = 2)
    private Float cardCost;
}
