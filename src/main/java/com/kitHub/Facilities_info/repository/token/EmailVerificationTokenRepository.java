package com.kitHub.Facilities_info.repository.token;

import com.kitHub.Facilities_info.domain.token.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByEmail(String email);
    Optional<EmailVerificationToken> findByToken(String token);
    void deleteByEmail(String email);
}
