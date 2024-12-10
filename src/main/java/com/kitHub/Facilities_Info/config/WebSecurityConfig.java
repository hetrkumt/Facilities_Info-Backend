package com.kitHub.Facilities_info.config;

import com.kitHub.Facilities_info.util.jwt.JwtProvider;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenAuthenticationManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@RequiredArgsConstructor
@Configuration

public class WebSecurityConfig {

    private final JwtProvider tokenProvider;
    private final TokenAuthenticationManager tokenAuthenticationManager;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");  // 필요한 도메인으로 제한할 수 있습니다.
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/templates/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // TokenAuthenticationFilter 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);



        http.authorizeRequests()
//                // Comments 관련 설정
//                .requestMatchers(HttpMethod.POST, "/comments/**").authenticated()
//                .requestMatchers(HttpMethod.PUT, "/comments/**").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/comments/**").authenticated()
//
//                // Posts 관련 설정
//                .requestMatchers(HttpMethod.POST, "/posts/**").authenticated()
//                .requestMatchers(HttpMethod.PUT, "/posts/**").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/posts/**").authenticated()
//
//                // Reviews 관련 설정
//                .requestMatchers(HttpMethod.POST, "/api/review/**").authenticated()
//                .requestMatchers(HttpMethod.PUT, "/api/review/**").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/api/review/**").authenticated()
//
//                // Reports 관련 설정
//                .requestMatchers(HttpMethod.POST, "/reports/**").authenticated()
//
//                // Admin 관련 설정
//                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // 나머지 요청에 대한 설정
                .anyRequest().permitAll();

//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Please log in to access this resource.");
//                });

        http.headers()
                .frameOptions().disable(); // H2 콘솔 사용을 위해 프레임 옵션 비활성화

//        http.exceptionHandling()
//                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
//                        new AntPathRequestMatcher("/api/**"));

        return http.build();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, tokenAuthenticationManager, authenticationProvider);
    }

//    @Bean
//    public AdminAccessFilter adminAccessFilter() {
//
//        return new AdminAccessFilter(authenticationProvider);
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
