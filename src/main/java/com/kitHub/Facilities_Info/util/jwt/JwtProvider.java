package com.kitHub.Facilities_info.util.jwt;

import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.repository.UserRepository;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Autowired
    private final JwtProperties jwtProperties;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthenticationProvider authenticationProvider;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        String snsIdOrEmail = user.getProvider().equals("local") ? user.getEmail() : user.getSnsId();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("snsIdOrEmail",snsIdOrEmail)
                .claim("provider",user.getProvider())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);
        Optional <User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return authenticationProvider.makeAuthenticationFrom(user);
        }
        else{
            return null;
        }
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
