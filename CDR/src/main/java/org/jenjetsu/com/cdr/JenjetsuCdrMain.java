package org.jenjetsu.com.cdr;

import org.jenjetsu.com.core.annotation.EnableMinio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySources({
        @PropertySource(value = "classpath:application-${spring.profile.active}.yml", ignoreResourceNotFound = true)
})
@EnableMinio
public class JenjetsuCdrMain {

    public static void main(String[] args) {
        SpringApplication.run(JenjetsuCdrMain.class, args);
    }
}
