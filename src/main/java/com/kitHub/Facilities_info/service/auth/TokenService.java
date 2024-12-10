package com.kitHub.Facilities_info.service.auth;

import com.kitHub.Facilities_info.domain.token.RefreshToken;
import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.repository.token.RefreshTokenRepository;
import com.kitHub.Facilities_info.service.UserService;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenAuthenticationManager;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenValidationResult;
import com.kitHub.Facilities_info.util.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private TokenAuthenticationManager tokenAuthenticationManager;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;



    public String refreshAccessToken(String refreshToken) {
        TokenValidationResult validationResult = tokenAuthenticationManager.validateToken(refreshToken);

        if (validationResult == TokenValidationResult.VALID) {
            Optional<RefreshToken> foundRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (foundRefreshToken.isPresent()) {
                Long userId = foundRefreshToken.get().getId();
                User user = userService.findById(userId);
                String newAccessToken = jwtProvider.generateToken(user, Duration.ofDays(1));
                return newAccessToken;
            }
        }

        // 로그 출력
        System.out.println("Token validation failed with reason: " + validationResult);

        return null;
    }
}
