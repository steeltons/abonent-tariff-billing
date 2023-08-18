package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "tariff")
public class Tariff implements Cloneable{

    @Id private Integer tariffCode;
    @Column(name = "description", length = 255, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String description;
    @Column(name = "base_cost", nullable = false)
    private Double baseCost;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TariffCallOption> callOptionList;

    @Override
    public Tariff clone(){
        try {
            Tariff clone = (Tariff) super.clone();
            clone.setRegion(this.region);
            clone.setCallOptionList(this.callOptionList);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Tariff update(Tariff newTariff) {
        Tariff pred = this.clone();
        if(newTariff.baseCost != null && !this.baseCost.equals(newTariff.baseCost)) {
            this.baseCost = newTariff.getBaseCost();
        }
        if(newTariff.region != null && !this.region.equals(newTariff.region)) {
            this.region = newTariff.region;
        }
        if(newTariff.callOptionList != null && newTariff.callOptionList.size() != 0) {
            this.callOptionList = newTariff.callOptionList;
        }
        return pred;
    }
}
