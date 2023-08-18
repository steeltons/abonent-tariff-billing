package org.jenjetsu.com.brt.service.implementation;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.entity.Region;
import org.jenjetsu.com.brt.entity.Tariff;
import org.jenjetsu.com.brt.entity.TariffCallOption;
import org.jenjetsu.com.brt.repository.RegionRepository;
import org.jenjetsu.com.brt.repository.TariffCallOptionRepository;
import org.jenjetsu.com.brt.repository.TariffRepository;
import org.jenjetsu.com.brt.service.TariffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRep;
    private final TariffCallOptionRepository tariffCallOptionRep;
    private final RegionRepository regionRep;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Tariff create(Tariff raw) {
        return transactionTemplate.execute((status) -> {
            try {
                tariffRep.save(raw);
                return raw;
            } catch (Exception e) {
                throw new RuntimeException("Error creating tariff");
            }
        });
    }

    @Override
    public List<Tariff> createAll(Collection<Tariff> rawCollection) {
        return transactionTemplate.execute((status) -> {
            List<Tariff> tariffs = new ArrayList<>();
            try {
                for(Tariff tariff : rawCollection) {
                    tariffRep.save(tariff);
                    tariffs.add(tariff);
                }
                return tariffs;
            } catch (Exception e) {
                throw new RuntimeException("Error creating tariffs");
            }
        });
    }

    @Override
    public Tariff readById(Integer integer) {
        if(!isExistById(integer)) {
            return new Tariff();
        }
        return tariffRep.findById(integer).get();
    }

    @Override
    public List<Tariff> readAll() {
        return tariffRep.findAll();
    }

    @Override
    public Tariff updateById(Tariff newObject, Integer integer) {
        if(!isExistById(newObject.getTariffCode())) {
            return new Tariff();
        }
        return transactionTemplate.execute((status) -> {
            Tariff updateableTariff = tariffRep.findById(newObject.getTariffCode()).get();
            Tariff pred = updateableTariff.update(newObject);
            return pred;
        });
    }

    @Override
    public boolean isExistById(Integer id) {
        return id != null && tariffRep.existsById(id);
    }

    @Override
    public Tariff getTariffByAbonentPhoneNumber(Long phoneNumber) {
        Tariff tariff = tariffRep.findByAbonentPhoneNumber(phoneNumber).orElseThrow(() -> new RuntimeException("Tariff id is not exist"));
        tariff = readById(tariff.getTariffCode());
        return tariff;
    }
}
