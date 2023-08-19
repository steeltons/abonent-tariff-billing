package org.jenjetsu.com.hrs;

import org.jenjetsu.com.core.annotation.EnableMinio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableCaching @EnableMinio
@Configuration
public class JenjetsuHrsMain {

    public static void main(String[] args) {
        SpringApplication.run(JenjetsuHrsMain.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("tariff-cache");
    }
}
