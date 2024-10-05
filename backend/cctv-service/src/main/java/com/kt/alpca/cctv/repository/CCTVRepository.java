package com.kt.alpca.cctv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.kt.alpca.cctv.model.CCTV;

@RepositoryRestResource
public interface CCTVRepository extends JpaRepository<CCTV, String> {

}
