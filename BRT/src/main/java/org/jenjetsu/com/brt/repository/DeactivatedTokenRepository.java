package org.jenjetsu.com.brt.repository;

import org.jenjetsu.com.brt.entity.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, String> {
}
