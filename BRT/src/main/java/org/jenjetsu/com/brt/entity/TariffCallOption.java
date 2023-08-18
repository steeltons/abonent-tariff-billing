package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "tariff_call_option")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"call_priority", "tariff_code", "call_type"})
})
public class TariffCallOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "call_type", nullable = false)
    @Min(1) @Max(2)
    private Byte callType;
    @Column(name = "minute_cost", nullable = false)
    @Min(0)
    private double minuteCost;
    //TODO change name
    @Column(name = "minute_buffer", nullable = false, columnDefinition = "INT4 DEFAULT 0")
    @Min(0)
    private int minuteBuffer;
    @Column(name = "option_price", nullable = false)
    @Min(0)
    private double optionPrice;
    @Column(name = "call_priority", nullable = false)
    @Min(0) @Max(10)
    private byte callPriority;
    @ManyToOne
    @JoinColumn(name = "tariff_code", nullable = false)
    private Tariff tariff;
}
