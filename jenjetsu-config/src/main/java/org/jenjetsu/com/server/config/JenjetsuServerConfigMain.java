package org.jenjetsu.com.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableConfigServer
public class JenjetsuServerConfigMain implements CommandLineRunner {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(JenjetsuServerConfigMain.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(environment.getProperty("EUREKA_HOST"));
        System.out.println(environment.getProperty("EUREKA_PORT"));
    }
}
