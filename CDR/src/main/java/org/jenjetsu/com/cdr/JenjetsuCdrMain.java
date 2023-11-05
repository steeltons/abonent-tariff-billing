package org.jenjetsu.com.cdr;

import org.jenjetsu.com.core.annotation.EnableMinio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableMinio
public class JenjetsuCdrMain {

    public static void main(String[] args) {
        SpringApplication.run(JenjetsuCdrMain.class, args);
    }
}
