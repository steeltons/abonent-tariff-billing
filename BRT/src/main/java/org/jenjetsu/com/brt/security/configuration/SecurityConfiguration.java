package org.jenjetsu.com.brt.security.configuration;

import org.jenjetsu.com.brt.security.provider.AdminAuthenticationProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationConfigurer jwtAuthenticationConfigurer;
    private final AuthenticationProvider abonentAuthenticationProvider;

    public SecurityConfiguration(JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
                                 @Qualifier("abonentAuthenticationProvider") AuthenticationProvider abonentAuthenticationProvider) {
        this.jwtAuthenticationConfigurer = jwtAuthenticationConfigurer;
        this.abonentAuthenticationProvider = abonentAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.apply(jwtAuthenticationConfigurer);
        return http
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(abonentAuthenticationProvider)
                .authenticationProvider(adminAuthenticationProvider())
                .sessionManagement((sessionManager) -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((httpRequests) ->
                        httpRequests
                                .requestMatchers("/api/v1/abonent/create").hasRole("ADMIN")
                                .requestMatchers("/api/v1/abonent/start-billing").hasRole("ADMIN")
                                .requestMatchers("/api/v1/abonent/get-not-blocked").hasAnyRole("ADMIN")
                                .anyRequest().hasAnyRole("ADMIN", "ABONENT"))
                .build();
    }

    @Bean("adminUserDetailsService")
    public UserDetailsService adminUserDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("1228")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean("adminAuthenticationProvider")
    public AuthenticationProvider adminAuthenticationProvider() {
        return new AdminAuthenticationProvider(adminUserDetailsService());
    }
}
