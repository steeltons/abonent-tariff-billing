package org.jenjetsu.com.brt.security.configuration;

import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Arrays;

@Configuration
public class AuthenticationManagerConfigurer {

    @Bean("inMemoryUserDetailsService")
    public UserDetailsService inMemoryUserDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("1228")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public AuthenticationManager authenticationManager(AbonentService abonentService) {
        return auth -> {
            String username = auth.getName();
            String password = auth.getCredentials().toString();
            if(username.startsWith("+")) {
                if(abonentService.authenticateAbonent(Long.parseLong(username.replace("+","")), password)) {
                    return new PreAuthenticatedAuthenticationToken(username, "nopass", Arrays.asList(new SimpleGrantedAuthority("ABONENT")));
                }
            } else {
                UserDetailsService adminService = inMemoryUserDetailsService();
                UserDetails ud = adminService.loadUserByUsername(username);
                if(ud != null) {
                    return new PreAuthenticatedAuthenticationToken(ud.getUsername(), ud.getPassword(), ud.getAuthorities());
                }
            }
            throw new BadCredentialsException(String.format("User wasn't found"));
        };
    }
}
