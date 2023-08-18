package org.jenjetsu.com.brt.repository;

import org.jenjetsu.com.brt.entity.TariffCallOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TariffCallOptionRepository extends JpaRepository<TariffCallOption, Long> {

    public List<TariffCallOption> findAllByTariffTariffCode(Integer tariffCode);
}
