package org.jenjetsu.com.brt.service;

import org.jenjetsu.com.brt.entity.DeactivatedToken;

public interface DeactivatedTokenService extends CreateInterface<DeactivatedToken>,
                                                 ReadInterface<DeactivatedToken, String>{

    public boolean isExistById(String id);
}
