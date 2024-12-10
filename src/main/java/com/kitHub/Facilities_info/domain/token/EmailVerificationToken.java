package com.kitHub.Facilities_info.domain.token;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "email_verification_tokens")
@Entity
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "token", length = 512, nullable = false)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    // 생성자
    public EmailVerificationToken(String email, String token, LocalDateTime expiryDate) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
        this.isVerified = false; // 초기 상태는 인증되지 않음
    }

    // 토큰 업데이트 메서드
    public EmailVerificationToken updateToken(String newToken, LocalDateTime newExpiryDate) {
        this.token = newToken;
        this.expiryDate = newExpiryDate;
        this.isVerified = false; // 새로운 토큰 발행 시 인증되지 않은 상태로 설정
        return this;
    }

    // 이메일 인증 완료 메서드
    public EmailVerificationToken verify() {
        this.isVerified = true; // 이메일 인증 완료 상태로 변경
        return this;
    }
}
