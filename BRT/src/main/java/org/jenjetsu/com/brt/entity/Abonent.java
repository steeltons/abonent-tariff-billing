package org.jenjetsu.com.brt.entity;

import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.CascadeType.MERGE;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "abonent")
@Builder
public class Abonent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID abonentId;
    @Column(name = "phone_number", nullable = false, unique = true)
    private Long phoneNumber;
    @Column(
            name = "balance", nullable = false, 
            precision = 6, scale = 2, columnDefinition = "NUMERIC(6,2) DEFAULT 0.0"
           )
    @Max(100000)
    private BigDecimal balance;
    @Column(name = "is_blocked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlocked;
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {MERGE, DETACH})
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "abonent_id")
    private List<AbonentPayload> payloadList;
}
