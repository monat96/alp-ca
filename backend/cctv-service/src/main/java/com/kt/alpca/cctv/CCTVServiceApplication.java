package com.kt.alpca.cctv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CCTVServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CCTVServiceApplication.class, args);
    }

}
