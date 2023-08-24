package org.jenjetsu.com.brt.security.service;

import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.security.TokenUser;
import org.jenjetsu.com.brt.security.token.Token;
import org.jenjetsu.com.brt.service.DeactivatedTokenService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final DeactivatedTokenService deactivatedTokenService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if(authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(token.getSubject(), "nopassword", true, true,
                    !deactivatedTokenService.isExistById(token.getId()) &&
                    token.getExpiredAt().isAfter(Instant.now()), true,
                    token.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList(),
                    token);
        }
        throw new UsernameNotFoundException("Principal must be of type Token");
    }
}
