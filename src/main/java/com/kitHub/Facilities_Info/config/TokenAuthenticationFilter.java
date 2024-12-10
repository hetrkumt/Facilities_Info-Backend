package com.kitHub.Facilities_info.config;

import com.kitHub.Facilities_info.domain.user.User;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenAuthenticationManager;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenValidationResult;
import com.kitHub.Facilities_info.util.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider tokenProvider;
    private final TokenAuthenticationManager tokenAuthenticationManager;
    private final AuthenticationProvider authenticationProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String ACCESS_TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 요청 정보 출력
        logRequestDetails(request);

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        if (token != null) {
            TokenValidationResult tokenValidationResult = tokenAuthenticationManager.validateToken(token);

            if (tokenValidationResult == TokenValidationResult.VALID) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                User user = (User) authentication.getPrincipal();

                if (user instanceof User && ((User) user).isBlocked()) {// 차단된 유저를 로그아웃 컨트롤러로 리다이렉트
                    response.sendRedirect("/api/auth/logout");
                    return;
                }

                authenticationProvider.setAuthenticationtoSecurityContextHolder(authentication);
            } else {
                System.out.println("tokenValidationResult = " + tokenValidationResult);
                //handleTokenValidationFailure(tokenValidationResult, response);

            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleTokenValidationFailure(TokenValidationResult tokenValidationResult, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        switch (tokenValidationResult) {
            case MISSING:
            case INVALID:
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Invalid token\"}");
//                break;
            case MALFORMED:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Malformed token\"}");
                break;
            case UNSUPPORTED:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Unsupported token\"}");
                break;
            case EXPIRED:
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\": \"Token expired\"}");
//                break;
            case SIGNATURE_INVALID:
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\": \"Invalid token signature\"}");
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\": \"An unexpected error occurred\"}");
                break;
        }
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            return authorizationHeader.substring(ACCESS_TOKEN_PREFIX.length());
        }
        return null;
    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                System.out.println(headerName + ": " + request.getHeader(headerName))
        );

    }
}
