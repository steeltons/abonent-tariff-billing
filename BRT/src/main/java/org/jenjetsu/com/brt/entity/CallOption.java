package org.jenjetsu.com.brt.entity;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;

import org.hibernate.annotations.Check;
import org.jenjetsu.com.brt.entity.enums.CallType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * Call option unusual fields description:
 * - callCost - cost per minute
 * - callBuffer - buffer for option (in minutes)
 *   Value 0 means unlimited time.
 * - callPriority - priority for call option (Highest - 0, Lowest - 10)
 * - areCallTypesLinked - connect callBuffers for input call and output call on same call priority
 *   or nearest priority in opposite callType 
 * Class description:
 * - Every tariff can have only one priority type fo INPUT and OUTPUT calls
 * - Zero priority only have unlimited time

 */
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "call_option")
@Table(uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tariff_id", "call_type", "call_priority"})
        }
      )
@Check(constraints = "(call_priority = 0 AND call_buffer = 0) OR (call_priority != 0 AND call_buffer != 0))")
public class CallOption {

    public final static Byte HIGHEST_CALL_PRIORITY = (byte) 0;
    public final static Byte LOWEST_CALL_PRIORITY = (byte) 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long callOptionId;
    @Column(name = "call_type", nullable = false)
    private CallType callType;
    @Column(
            name = "call_cost", nullable = false,
            precision = 4, length = 2, columnDefinition = "NUMERIC(4,2) DEFAULT 0.0"
           )
    private Float callCost;
    @Column(name = "call_buffer", nullable = false, columnDefinition = "INT2 DEFAULT 0")
    private Short callBuffer;
    @Column(name = "call_priority", nullable = false, columnDefinition = "INT2 DEFAULT 0")
    private Byte callPriority;
    @Column(name = "are_call_types_linked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean areCallTypesLinked;
    @Column(
            name = "cost", nullable = false, 
            precision = 5, length = 2, columnDefinition = "NUMERIC(5,2) DEFAULT 25.0"
           )
    @Min(25) @Max(500)
    private Float cost;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;
}
