package com.kitHub.Facilities_info.service.auth;

import com.kitHub.Facilities_info.domain.token.EmailVerificationToken;
import com.kitHub.Facilities_info.repository.token.EmailVerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Transactional
    public void sendVerificationEmail(String email) {
        Optional<EmailVerificationToken> existingTokenOpt = tokenRepository.findByEmail(email);

        String token;
        // 기존 토큰이 존재하면 업데이트
        if (existingTokenOpt.isPresent()) {
            EmailVerificationToken existingToken = existingTokenOpt.get();
            token = UUID.randomUUID().toString();
            existingToken.setToken(token);
            existingToken.setExpiryDate(LocalDateTime.now().plus(Duration.ofHours(24)));
            existingToken.setVerified(false);
            tokenRepository.save(existingToken);
        } else {
            // 새로운 토큰 생성
            token = UUID.randomUUID().toString();
            EmailVerificationToken verificationToken = new EmailVerificationToken(email, token, LocalDateTime.now().plus(Duration.ofHours(24)));
            tokenRepository.save(verificationToken);
        }

        // 이메일 전송
        //String verificationUrl = "http://3.34.105.70:8080/api/verifyEmail/" + token;
        String verificationUrl = "http://localhost:8080/api/verifyEmail/" + token;
        String message = loadHtmlTemplate("classpath:templates/email-template.html");
        message = message.replace("{verificationUrl}", verificationUrl);

        MimeMessage emailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(emailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(email);
            helper.setSubject("Email Verification");
            helper.setText(message, true);
            mailSender.send(emailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String loadHtmlTemplate(String filePath) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    public String verifyToken(String token) {
        Optional<EmailVerificationToken> verificationTokenOpt = tokenRepository.findByToken(token);
        if (verificationTokenOpt.isPresent()) {
            EmailVerificationToken verificationToken = verificationTokenOpt.get();
            if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                return "Token expired";
            } else if (verificationToken.isVerified()) {
                return "Token already verified";
            } else {
                verificationToken.verify();
                tokenRepository.save(verificationToken);
                return "Token verified";
            }
        } else {
            return "Invalid token";
        }
    }

    public boolean isEmailVerified(String email) {
        Optional<EmailVerificationToken> verificationToken = tokenRepository.findByEmail(email);
        return verificationToken.isPresent() && verificationToken.get().isVerified();
    }

    @Transactional
    public void deleteVerificationTokenByEmail(String email) {
        tokenRepository.deleteByEmail(email);
    }
}
