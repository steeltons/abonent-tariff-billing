package org.jenjetsu.com.brt.repository;

import org.jenjetsu.com.brt.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Integer> {

    @Query(
            value = "SELECT t.* FROM abonent a " +
                    "LEFT JOIN tariff t ON t.tariff_code = a.tariff_id " +
                    "WHERE a.phone_number = :phone ",
            nativeQuery = true
          )
    public Optional<Tariff> findByAbonentPhoneNumber(@Param("phone") Long phoneNumber);
}
