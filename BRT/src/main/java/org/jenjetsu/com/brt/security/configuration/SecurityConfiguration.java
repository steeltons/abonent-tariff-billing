package org.jenjetsu.com.brt.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationConfigurer jwtAuthenticationConfigurer;

    public SecurityConfiguration(JwtAuthenticationConfigurer jwtAuthenticationConfigurer) {
        this.jwtAuthenticationConfigurer = jwtAuthenticationConfigurer;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.apply(jwtAuthenticationConfigurer);
        return http
                .httpBasic(Customizer.withDefaults())
                .sessionManagement((sessionManager) -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((httpRequests) ->
                        httpRequests
                                .requestMatchers("/api/v1/abonent/create").hasRole("ADMIN")
                                .requestMatchers("/api/v1/abonent/start-billing").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .build();
    }

}