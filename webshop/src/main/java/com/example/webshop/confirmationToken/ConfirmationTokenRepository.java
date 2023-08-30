package com.example.webshop.confirmationToken;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
    Optional<ConfirmationToken> findByToken(String token);
//    @Transactional
    @Modifying
    @Query("""
    UPDATE ConfirmationToken c set c.confirmedAt = ?2 where c.token = ?1
    """)
    int setConfirmedAt(String token, LocalDateTime confirmDate);
}
