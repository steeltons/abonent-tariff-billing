package org.jenjetsu.com.brt.service.implementation;

import org.jenjetsu.com.brt.entity.CallOption;
import org.jenjetsu.com.brt.service.CallOptionService;
import org.springframework.stereotype.Service;

@Service
public class CallOptionServiceImpl extends AbstractDAORepository<CallOption, Long>
                                   implements CallOptionService {
    
    public CallOptionServiceImpl() {
        super(CallOption.class);
    }

}
