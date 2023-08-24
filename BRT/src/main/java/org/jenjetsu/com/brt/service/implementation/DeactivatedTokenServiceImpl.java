package org.jenjetsu.com.brt.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.entity.DeactivatedToken;
import org.jenjetsu.com.brt.repository.DeactivatedTokenRepository;
import org.jenjetsu.com.brt.service.DeactivatedTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DeactivatedTokenServiceImpl implements DeactivatedTokenService {

    private final DeactivatedTokenRepository deactivatedTokenRep;
    private final TransactionTemplate transactionTemplate;

    @Override
    public DeactivatedToken create(DeactivatedToken raw) {
        return transactionTemplate.execute((st) -> {
            try {
                deactivatedTokenRep.save(raw);
                return raw;
            } catch (Exception e) {
                log.error("Error saving deactivated token.");
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<DeactivatedToken> createAll(Collection<DeactivatedToken> rawCollection) {
        return transactionTemplate.execute((st) -> {
            List<DeactivatedToken> deactivatedTokenList = new ArrayList<>();
            for(DeactivatedToken token : rawCollection) {
                try {
                    deactivatedTokenRep.save(token);
                    deactivatedTokenList.add(token);
                } catch (Exception e) {
                    log.error("Error create deactivated token {}", token.toString());
                    throw new RuntimeException(e);
                }
            }
            return deactivatedTokenList;
        });
    }

    @Override
    public boolean isExistById(String id) {
        return id != null && !id.isBlank() && deactivatedTokenRep.existsById(id);
    }

    @Override
    public DeactivatedToken readById(String s) {
        try {
            if(isExistById(s)) {
                Optional<DeactivatedToken> optional = deactivatedTokenRep.findById(s);
                if(optional.isPresent()) {
                    return optional.get();
                }
            }
            throw new RuntimeException(String.format("Token with id %s not found", s));
        } catch (Exception e) {
            log.error("Token with id {} not exist", s);
            throw e;
        }
    }

    @Override
    public List<DeactivatedToken> readAll() {
        return deactivatedTokenRep.findAll();
    }
}
