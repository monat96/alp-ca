package com.kt.alpca.cctv.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kt.alpca.cctv.domain.model.CCTV;

@RepositoryRestResource(collectionResourceRel = "cctvs", path = "cctvs")
public interface CCTVRepository extends JpaRepository<CCTV, Long> {

}
