package org.jenjetsu.com.brt.service.implementation;

import java.util.UUID;

import org.jenjetsu.com.brt.entity.DeactivatedToken;
import org.jenjetsu.com.brt.service.DeactivatedTokenService;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeactivatedTokenServiceImpl extends AbstractDAORepository<DeactivatedToken, UUID>
                                         implements DeactivatedTokenService {

    public DeactivatedTokenServiceImpl() {
        super(DeactivatedToken.class);
    }
    
}
