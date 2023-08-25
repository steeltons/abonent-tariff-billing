package org.jenjetsu.com.brt.security.provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class AdminAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService adminService;

    public AdminAuthenticationProvider(UserDetailsService adminService) {
        this.adminService = adminService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName() != null ? authentication.getName() : "";
        UserDetails userDetails = adminService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new UsernameNotFoundException("User is not exist in admin database");
        }
        String password = (String) authentication.getCredentials();
        if(!userDetails.getPassword().equals(password)) {
            throw new BadCredentialsException(String.format("Not correct password for admin %s", username));
        }
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return UsernamePasswordAuthenticationToken.authenticated(
                username,
                "nopassword",
                authorityList
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
