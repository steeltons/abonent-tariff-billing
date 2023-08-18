package org.jenjetsu.com.brt.repository;

import org.jenjetsu.com.brt.entity.AbonentPayload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbonentPayloadRepository extends JpaRepository<AbonentPayload, Long> {
}
