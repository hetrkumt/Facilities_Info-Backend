package com.kitHub.Facilities_info.controller;


import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.dto.UserResponse;
import com.kitHub.Facilities_info.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);

            // 사용자 상태에 따른 메시지 반환
            String message;
            if (user.isBlocked()) {
                message = "차단된 사용자입니다.";
            } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
                message = "관리자입니다.";
            } else {
                message = "일반 사용자입니다.";
            }

            // UserResponse 객체 생성 및 필드 설정
            UserResponse userResponse = new UserResponse();
            userResponse.setUser(user);
            userResponse.setMessage(message);

            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
