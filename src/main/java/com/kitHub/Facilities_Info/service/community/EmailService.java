package com.kitHub.Facilities_info.service.community;

import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    public void sendEmail(String email, String subject, String message) {
        User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
        String senderEmail = user != null ? user.getEmail() : "unknown@unknown.com";

        MimeMessage emailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(emailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setTo(email);
            helper.setFrom(senderEmail); // 보낸 사람 설정
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(emailMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

    public void sendQuestionEmail(String email, String questionType, String questionContent) {
        User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
        System.out.println(user);
        String senderEmail = user != null ? user.getEmail() : "unknown@unknown.com";

        System.out.println(senderEmail);
        // 이메일 템플릿 로드
        String message = loadHtmlTemplate("classpath:templates/question-template.html");
        message = message.replace("{questionType}", questionType);
        message = message.replace("{questionContent}", questionContent);
        message = message.replace("{senderEmail}", senderEmail);

        // 이메일 전송
        sendEmail(email, "질문 유형: " + questionType, message);
    }

    private String loadHtmlTemplate(String filePath) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("이메일 템플릿 로드 실패", e);
        }
    }
}
