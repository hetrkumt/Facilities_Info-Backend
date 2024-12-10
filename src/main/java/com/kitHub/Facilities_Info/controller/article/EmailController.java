package com.kitHub.Facilities_info.controller.article;

import com.kitHub.Facilities_info.service.community.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String message) {
        try {
            emailService.sendEmail(email, subject, message);
            return ResponseEntity.ok("이메일이 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("이메일 전송에 실패했습니다. 나중에 다시 시도해 주세요.");
        }
    }

    @PostMapping("/sendQuestion")
    public ResponseEntity<String> sendQuestionEmail(@RequestBody QuestionRequest questionRequest) {
        System.out.println(questionRequest.getEmail()+questionRequest.getQuestionType()+questionRequest.getQuestionContent());
        try {
            emailService.sendQuestionEmail(questionRequest.getEmail(), questionRequest.getQuestionType(), questionRequest.getQuestionContent());
            return ResponseEntity.ok("질문 이메일이 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("질문 이메일 전송에 실패했습니다. 나중에 다시 시도해 주세요.");
        }
    }
}

class QuestionRequest {
    private String email;
    private String questionType;
    private String questionContent;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
}
