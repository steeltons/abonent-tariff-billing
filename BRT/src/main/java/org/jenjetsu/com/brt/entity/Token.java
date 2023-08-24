package org.jenjetsu.com.brt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String id;
    private String subject;
    private List<String> authorities;
    private Instant createAt;
    private Instant expiredAt;
}
