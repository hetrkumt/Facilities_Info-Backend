package com.kitHub.Facilities_info.controller.auth;

import com.kitHub.Facilities_info.dto.token.CreateAccessTokenRequest;
import com.kitHub.Facilities_info.dto.token.CreateAccessTokenResponse;
import com.kitHub.Facilities_info.service.auth.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/token")
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<CreateAccessTokenResponse> refreshAccessToken(@RequestBody CreateAccessTokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        if (newAccessToken != null) {
            CreateAccessTokenResponse response = new CreateAccessTokenResponse(newAccessToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(null);  // 401 Unauthorized
        }
    }
}
