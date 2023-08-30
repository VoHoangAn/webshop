package com.example.webshop.jwtToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken,Long> {
    Optional<JwtToken> findByToken(String token);

  /*  @Query(value = """
    SELECT t from token inner join user u\s 
    on t.id = u.id\s 
    where u.id = :id and (t.revoked = false or t.expired = false )\s
    """)*/
  @Query(value = """
      select t from JwtToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<JwtToken> findAllValidTokenByUser(Long id);
}
