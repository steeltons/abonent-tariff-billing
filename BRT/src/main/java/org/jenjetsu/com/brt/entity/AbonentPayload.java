package org.jenjetsu.com.brt.entity;


import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import org.jenjetsu.com.brt.entity.enums.CallType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "abonent_payload")
@Builder
public class AbonentPayload {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payloadId;
    @Column(name = "call_to", nullable = false)
    private Long callTo;
    @Column(name = "call_type")
    private CallType callType;
    @Column(name = "cost", nullable = false, precision = 4, scale = 2)
    private BigDecimal cost;
    @Column(name = "start_calling_time", nullable = false)
    private Timestamp startCallingTime;
    @Column(name = "end_calling_time", nullable = false)
    private Timestamp endCallingTime;
    @Column(name = "duration", nullable = false)
    private Time duration;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "abonent_id", nullable = false)
    private Abonent abonent;
}
