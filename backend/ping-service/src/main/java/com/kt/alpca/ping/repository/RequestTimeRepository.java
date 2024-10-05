package com.kt.alpca.ping.repository;

import com.kt.alpca.ping.model.RequestTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestTimeRepository extends JpaRepository<RequestTime, Long> {
    RequestTime findTopByOrderByTimeDesc();
}
