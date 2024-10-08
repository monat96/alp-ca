package com.kt.alpca.healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HealthCheckServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCheckServiceApplication.class, args);
	}

}
