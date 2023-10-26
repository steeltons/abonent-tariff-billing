package org.jenjetsu.com.brt.entity;

import java.sql.Timestamp;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "deactivated_token")
@Builder
public class DeactivatedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tokenId;
    @Column(name = "keep_until", nullable = false)
    private Timestamp keepUntil;
}
