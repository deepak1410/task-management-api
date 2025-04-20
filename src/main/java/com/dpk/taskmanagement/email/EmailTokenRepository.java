package com.dpk.taskmanagement.email;

import com.dpk.taskmanagement.email.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    Optional<EmailToken> findByTokenAndUsedFalseAndExpiryDateAfter(String token, LocalDateTime now);
}
