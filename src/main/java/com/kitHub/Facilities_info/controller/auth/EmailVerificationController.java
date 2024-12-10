package com.kitHub.Facilities_info.controller.auth;

import com.kitHub.Facilities_info.service.auth.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.MailException;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api")
public class EmailVerificationController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/emailVerify/{email}")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable String email) {
        try {
            emailVerificationService.sendVerificationEmail(email);
            return ResponseEntity.ok("인증 이메일이 성공적으로 전송되었습니다.");
        } catch (MailException e) {
            // 메일 전송 실패 처리
            return ResponseEntity.status(500).body("인증 이메일 전송에 실패했습니다. 나중에 다시 시도해 주세요.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(500).body("예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    }

    @GetMapping("/verifyEmail/{token}")
    public ModelAndView verifyEmail(@PathVariable String token) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String verificationResult = emailVerificationService.verifyToken(token);
            switch (verificationResult) {
                case "Token verified":
                    modelAndView.setViewName("emailVerified"); // 성공 페이지로 리디렉션
                    modelAndView.addObject("message", "이메일이 성공적으로 인증되었습니다. 회원가입을 완료해 주세요.");
                    break;
                case "Token expired":
                    modelAndView.setViewName("emailVerificationError"); // 실패 페이지로 리디렉션
                    modelAndView.addObject("message", "토큰이 만료되었습니다.");
                    break;
                case "Token already verified":
                    modelAndView.setViewName("emailVerificationError"); // 실패 페이지로 리디렉션
                    modelAndView.addObject("message", "토큰이 이미 인증되었습니다.");
                    break;
                default:
                    modelAndView.setViewName("emailVerificationError"); // 실패 페이지로 리디렉션
                    modelAndView.addObject("message", "잘못된 토큰입니다.");
                    break;
            }
        } catch (Exception e) {
            modelAndView.setViewName("emailVerificationError"); // 기타 예외 발생 시 리디렉션
            modelAndView.addObject("message", "예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
        return modelAndView;
    }

    @GetMapping("/checkEmailVerify/{email}")
    public ResponseEntity<String> checkEmailVerification(@PathVariable String email) {
        try {
            boolean isVerified = emailVerificationService.isEmailVerified(email);
            if (isVerified) {
                // 이메일 인증 확인 후 토큰 삭제
                emailVerificationService.deleteVerificationTokenByEmail(email);
                return ResponseEntity.ok("이메일이 인증되었으며, 토큰이 삭제되었습니다.");
            } else {
                return ResponseEntity.status(400).body("이메일이 인증되지 않았습니다.");
            }
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(500).body("예기치 않은 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        }
    }
}
