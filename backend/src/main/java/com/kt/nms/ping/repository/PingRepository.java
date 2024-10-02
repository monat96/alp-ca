package com.kt.nms.ping.repository;

import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.ping.model.Ping;
import com.kt.nms.requestTime.model.RequestTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PingRepository extends JpaRepository<Ping, Long> {
    List<Ping> findByCctv(CCTV cctv);
    List<Ping> findByRequestTime(RequestTime lastestRequestTime);
    Optional<Ping> findTopByCctvOrderByCreatedAtDesc(CCTV cctv);
}
