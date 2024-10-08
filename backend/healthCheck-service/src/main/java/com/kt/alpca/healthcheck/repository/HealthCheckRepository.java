package com.kt.alpca.healthcheck.repository;

import com.kt.alpca.healthcheck.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
}
