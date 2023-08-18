package org.jenjetsu.com.brt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "region")
public class Region {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regionId;
    @Column(name = "region_name", length = 40, nullable = false)
    private String regionName;
}
