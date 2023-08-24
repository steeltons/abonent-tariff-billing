package org.jenjetsu.com.brt.security;

import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final AbonentService abonentService;

    private final UserDetailsService adminService;

    public CustomUserDetailsService(AbonentService abonentService,
                                    @Qualifier("inMemoryUserDetailsService") UserDetailsService adminService) {
        this.abonentService = abonentService;
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith("+")) {
            return abonentService.loadByPhoneNumber(Long.parseLong(username.replace("+", "")));
        } else {
            return adminService.loadUserByUsername(username);
        }
    }
}
