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
    @Column(name = "minute_cost", nullable = false, scale = 4, length = 2)
    private Float minuteCost;
    @Column(name = "minute_buffer", nullable = false, columnDefinition = "INT2 DEFAULT 0")
    private Short minuteBuffer;
}
