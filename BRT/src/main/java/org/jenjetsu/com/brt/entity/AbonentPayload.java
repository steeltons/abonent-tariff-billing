package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.sql.Date;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "abonent_payload")
@Check(constraints = "start_calling_time <= end_calling_time")
public class AbonentPayload {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payloadId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abonent_id", nullable = false)
    private Abonent abonent;
    @Column(name = "call_type", nullable = false)
    private Byte callType;
    @Column(name = "call_to", nullable = false)
    private Long callTo;
    @Column(name = "start_calling_time", nullable = false)
    private Date startCallingTime;
    @Column(name = "end_calling_time", nullable = false)
    private Date endCallingTime;
    @Column(name = "duration", nullable = false)
    private Time duration;
    @Column(name = "cost", nullable = false)
    private Float cost;
}
