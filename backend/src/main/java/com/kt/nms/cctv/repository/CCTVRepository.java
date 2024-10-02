package com.kt.nms.cctv.repository;

import com.kt.nms.cctv.model.CCTV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CCTVRepository extends JpaRepository<CCTV, Long> {
    Optional<CCTV> findByCctvId(String cctvId);
}
