package com.kitHub.Facilities_info.controller.auth;


import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.dto.LoginSuccessRespone;
import com.kitHub.Facilities_info.dto.auth.AddLocalUserRequest;
import com.kitHub.Facilities_info.dto.auth.AddOauthUserRequest;
import com.kitHub.Facilities_info.dto.auth.LocalLoginRequest;
import com.kitHub.Facilities_info.dto.auth.OAuthLoginRequest;
import com.kitHub.Facilities_info.repository.token.RefreshTokenRepository;
import com.kitHub.Facilities_info.service.auth.LoginService;
import com.kitHub.Facilities_info.service.facility.ReviewService;
import com.kitHub.Facilities_info.service.UserService;

import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import com.kitHub.Facilities_info.util.Authentication.loginAuthentication.AuthenticationResult;
import com.kitHub.Facilities_info.util.jwt.JwtProvider;
import com.kitHub.Facilities_info.util.login.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable String email) {
        boolean exists = userService.checkEmailExists(email);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용 중인 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }


    @GetMapping("/checkNickname/{nickname}")
    public ResponseEntity<String> checkNickname(@PathVariable String nickname) {
        boolean exists = userService.checkNicknameExists(nickname);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용 중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
    }

    @PostMapping("/signup/local")
    public ResponseEntity<?> registerLocalUser(@RequestBody AddLocalUserRequest user) {
        Long userResult = userService.saveLocal(user);
        if (userResult == null) {
            return ResponseEntity.badRequest().body("계정이 이미 존재 합니다");
        } else {
            return ResponseEntity.ok("회원 가입이 완료되었습니다");
        }
    }

    @PostMapping("/signup/oauth")
    public ResponseEntity<?> registerOAuthUser(@RequestBody AddOauthUserRequest user) {
        Long userResult = userService.saveOAuth(user);
        if (userResult == null) {
            return ResponseEntity.badRequest().body("계정이 이미 존재 합니다");
        } else {
            return ResponseEntity.ok("회원 가입이 완료되었습니다");
        }
    }

    @PostMapping("/local/login")
    public ResponseEntity<LoginSuccessRespone> localLogin(@RequestBody LocalLoginRequest dto) {
        AuthenticationResult result = loginService.localLogin(dto);
        return handleAuthenticationResult(result);
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<LoginSuccessRespone> OAuthLogin(@RequestBody OAuthLoginRequest dto) {
        AuthenticationResult result = loginService.OAuthLogin(dto);
        return handleAuthenticationResult(result);
    }

    private ResponseEntity<LoginSuccessRespone> handleAuthenticationResult(AuthenticationResult result) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> tokens = new HashMap<>();
        LoginSuccessRespone loginSuccessRespone = new LoginSuccessRespone();
        switch (result) {
            case SUCCESS:
                tokens = loginSuccessHandler.makeTokensOnAuthenticationSuccess();
                String accessToken = tokens.get("accessToken");
                String refreshToken = tokens.get("refreshToken");
                User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
                headers.add("AccessToken", accessToken);
                headers.add("RefreshToken", refreshToken);
                // 디버그 로그 추가
                System.out.println("Access Token: " + accessToken);
                System.out.println("Refresh Token: " + refreshToken);
                System.out.println("Response Headers: " + headers);

                // 사용자 상태에 따른 메시지 반환
                String message;
                if (user.isBlocked()) {
                    message = "차단된 사용자입니다.";
                } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    message = "관리자입니다.";
                } else {
                    message = "일반 사용자입니다.";
                }

                loginSuccessRespone.setUser(user);
                loginSuccessRespone.setAccessToken(accessToken);
                loginSuccessRespone.setRefreshToken(refreshToken);
                loginSuccessRespone.setMessage(message);

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(loginSuccessRespone);

            case USER_NOT_FOUND:
                loginSuccessRespone.setMessage("User not found");
                return ResponseEntity.status(404).body(loginSuccessRespone);
            case INVALID_PASSWORD:
                loginSuccessRespone.setMessage("Invalid password");
                return ResponseEntity.status(401).body(loginSuccessRespone);
            default:
                loginSuccessRespone.setMessage("Authentication failed");
                return ResponseEntity.status(500).body(loginSuccessRespone);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationProvider.getAuthenticationFromSecurityContextHolder();
        if (authentication != null) {
            User user = authenticationProvider.getUserInfoFromSecurityContextHolder();

            refreshTokenRepository.deleteByUserId(user.getId());
            // RefreshToken 삭제
            System.out.println("User ID: " + user.getId() + "refresh token deleted");

            if ("true".equals(request.getParameter("blocked"))) {
                return ResponseEntity.ok("차단된 사용자입니다.");
            }

            new SecurityContextLogoutHandler().logout(request, response, authentication);

            System.out.println("Logout successful for user ID: " + user.getId());
            return ResponseEntity.ok("Logout successful");
        }

        System.out.println("No user is currently logged in"); // 로그 추가
        return ResponseEntity.status(400).body("No user is currently logged in");
    }
}



