package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "abonent")
public class Abonent implements Cloneable{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abonentId;
    @Column(name = "phone_number", nullable = false, unique = true)
    @Min(79000000000l) @Max(89999999999l)
    private Long phoneNumber;
    @Column(name = "balance", columnDefinition = "NUMERIC(6,2) DEFAULT 0.0", precision = 6, scale = 2)
    @Min(-100000)
    private Double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tariff_id", nullable = true)
    private Tariff tariff;
    @OneToMany(fetch = FetchType.LAZY)
    private List<AbonentPayload> payloads;
    @Column(name = "blocked", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isBlocked;

    public double increaseBalance(double increment) {
        double prev = this.balance;
        this.balance += increment;
        return prev;
    }

    @Override
    public Abonent clone(){
        try {
            Abonent abonent = (Abonent) super.clone();
            abonent.setTariff(this.getTariff());
            return abonent;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public double decreaseBalance(double decrement) {
        return this.increaseBalance(-decrement);
    }

    public Abonent update(Abonent newAbonent) {
        Abonent pred = this.clone();
        if(newAbonent.balance != null && !this.balance.equals(newAbonent.balance)) {
            this.balance = newAbonent.balance;
        }
        if(newAbonent.tariff != null && !this.tariff.equals(newAbonent.tariff)) {
            this.tariff = newAbonent.tariff;
        }
        return pred;
    }
}
