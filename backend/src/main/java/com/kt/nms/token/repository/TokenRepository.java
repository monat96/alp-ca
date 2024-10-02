package com.kt.nms.token.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kt.nms.token.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
  @Query(value = """
      SELECT t FROM Token t INNER JOIN User u
      ON t.user.userId = u.userId
      WHERE u.userId = :userId AND (t.expired = false OR t.revoked = false)
      """)
  List<Token> findAllValidTokenByUser(String userId);

  Optional<Token> findByToken(String token);

}