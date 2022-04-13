package com.en.remembrance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class RemembranceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemembranceApplication.class, args);
    }

}
