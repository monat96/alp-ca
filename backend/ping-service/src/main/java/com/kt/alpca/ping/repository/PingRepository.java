package com.kt.alpca.ping.repository;

import com.kt.alpca.ping.model.Ping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PingRepository extends JpaRepository<Ping, Long> {
}
