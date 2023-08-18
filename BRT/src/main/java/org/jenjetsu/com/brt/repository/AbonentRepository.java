package org.jenjetsu.com.brt.repository;

import org.jenjetsu.com.brt.entity.Abonent;
import org.jenjetsu.com.brt.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbonentRepository extends JpaRepository<Abonent, Long> {

    public boolean existsByPhoneNumber(Long phoneNumber);
    public Optional<Abonent> findByPhoneNumber(Long phonenNumber);

    @Query(
            value = "SELECT * " +
                    "FROM abonent " +
                    "WHERE NOT blocked",
            nativeQuery = true)
    public List<Abonent> findAllNotBlockedAbonents();
}
