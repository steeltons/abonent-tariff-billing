package org.jenjetsu.com.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class JenjetsuServerConfigMain {
    public static void main(String[] args) {
        SpringApplication.run(JenjetsuServerConfigMain.class, args);
    }

}
