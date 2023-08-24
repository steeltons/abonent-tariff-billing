package org.jenjetsu.com.brt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "deactivated_token")
@Check(constraints = "keep_until > now()")
public class DeactivatedToken {

    @Id
    @Column(length = 36)
    private String id;
    @Column(name = "keep_until", nullable = false)
    private Timestamp keepUntil;

}
