package org.jenjetsu.com.brt;

import org.jenjetsu.com.core.annotation.EnableMinio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableMinio
@EnableCaching
public class JenjetsuBrtMain {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(JenjetsuBrtMain.class, args);

    }

}
