package org.jenjetsu.com.brt.entity;

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
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tariff")
@Builder
public class Tariff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tariffId;
    @Column(name = "name", length = 72, unique = true, nullable = false)
    private String name;
    @Column(name = "description", length = 510, nullable = true)
    private String description;
    @Column(name = "base_cost", nullable = false, precision = 6, scale = 2)
    @Min(0) @Max(3000)
    private Float baseCost;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tariff_id")
    private List<CallOptionCard> callOptionCardList;
}
