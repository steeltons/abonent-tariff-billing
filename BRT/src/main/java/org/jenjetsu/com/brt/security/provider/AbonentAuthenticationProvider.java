package org.jenjetsu.com.brt.security.provider;

import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Component
public class AbonentAuthenticationProvider implements AuthenticationProvider {

    private final AbonentService abonentService;

    public AbonentAuthenticationProvider(AbonentService abonentService) {
        this.abonentService = abonentService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!supports(authentication.getClass())) {
            return null;
        }
        String rawPhoneNumber = this.getName(authentication);
        if(rawPhoneNumber.startsWith("+")) {
            Long phoneNumber = Long.parseLong(rawPhoneNumber.substring(1));
            if (!abonentService.isExistByPhoneNumber(phoneNumber)) {
                throw new UsernameNotFoundException(String.format("Abonent with phone number %d is not exist.", phoneNumber));
            }
            String password = (String) authentication.getCredentials();
            if(!abonentService.authenticateAbonent(phoneNumber, password)) {
                throw new BadCredentialsException(String.format("Not correct password for abonent %d", phoneNumber));
            }
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_ABONENT"));
            return UsernamePasswordAuthenticationToken.authenticated(
                    Long.toString(phoneNumber),
                    "nopassword",
                    authorityList
            );
        }
        return null;
    }

    private String getName(Authentication auth) {
        return auth.getName() == null ? "" : auth.getName();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
