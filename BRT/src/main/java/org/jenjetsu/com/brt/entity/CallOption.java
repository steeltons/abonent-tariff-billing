package org.jenjetsu.com.brt.entity;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;

import lombok.*;
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

import java.math.BigDecimal;

/**
 * Simple call option
 *
 */
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "call_option")
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"minute_cost", "minute_buffer"}))
public class CallOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long callOptionId;
    @Column(name = "minute_cost", nullable = false, precision = 4, scale = 2)
    private BigDecimal minuteCost;
    @Column(name = "minute_buffer", nullable = false, columnDefinition = "INT2 DEFAULT 0")
    private Short minuteBuffer;

    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof CallOption)) return false;
        CallOption c = (CallOption) o;
        if(c.callOptionId != null) return c.getCallOptionId().equals(this.callOptionId);
        if(c.minuteCost != null) return c.minuteCost.equals(this.minuteCost);
        if(c.minuteBuffer != null) return c.minuteBuffer.equals(this.minuteBuffer);
        return false;
    }
}
