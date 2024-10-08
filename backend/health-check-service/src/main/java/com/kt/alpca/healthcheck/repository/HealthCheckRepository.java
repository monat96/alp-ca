package com.kt.alpca.healthcheck.repository;

import com.kt.alpca.healthcheck.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "health-checks", path = "health-checks")
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
}
