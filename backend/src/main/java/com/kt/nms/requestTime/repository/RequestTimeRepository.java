package com.kt.nms.requestTime.repository;

import com.kt.nms.requestTime.model.RequestTime;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RequestTimeRepository extends JpaRepository<RequestTime, Long> {
    RequestTime findTopByOrderByRequestTimeDesc();
}
