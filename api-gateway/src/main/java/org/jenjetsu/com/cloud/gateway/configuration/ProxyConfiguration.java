package org.jenjetsu.com.cloud.gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocator locator = builder.routes()
                .route()
    }
}
