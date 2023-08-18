package org.jenjetsu.com.brt;

import org.jenjetsu.com.core.annotation.EnableMinio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMinio
public class JenjetsuBrtMain {

    public static void main(String[] args) {
        SpringApplication.run(JenjetsuBrtMain.class, args);
    }
}
