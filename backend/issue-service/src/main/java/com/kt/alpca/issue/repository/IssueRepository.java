package com.kt.alpca.issue.repository;

import com.kt.alpca.issue.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "issues", path = "issues")
public interface IssueRepository extends JpaRepository<Issue, Long> {
}
