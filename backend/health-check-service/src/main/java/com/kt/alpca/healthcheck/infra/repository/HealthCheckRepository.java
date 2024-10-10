package com.kt.alpca.healthcheck.infra.repository;

import com.kt.alpca.healthcheck.domain.model.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "health-checks", path = "health-checks")
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
}
