package org.rsc.sporty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SportyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SportyApplication.class, args);
    }
}