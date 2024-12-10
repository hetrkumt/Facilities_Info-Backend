//package com.kitHub.Facilities_info.config;
//
//import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class AdminAccessFilter extends OncePerRequestFilter {
//    private final AuthenticationProvider authenticationProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        Authentication authentication = authenticationProvider.getAuthenticationFromSecurityContextHolder();
//
//        System.out.println("fafewff");
//        if (authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            String method = request.getMethod();
//            String requestURI = request.getRequestURI();
//
//            if ("DELETE".equalsIgnoreCase(method)) {
//                if (requestURI.matches("/api/review/delete/\\d+") || requestURI.matches("/posts/\\d+") || requestURI.matches("/comments/\\d+")) {
//                    if (requestURI.startsWith("/api/review/delete/")) {
//                        response.sendRedirect("/api/admin/reviews/delete/" + extractId(requestURI));
//                    } else if (requestURI.startsWith("/posts/")) {
//                        response.sendRedirect("/api/admin/posts/delete/" + extractId(requestURI));
//                    } else if (requestURI.startsWith("/comments/")) {
//                        response.sendRedirect("/api/admin/comments/delete/" + extractId(requestURI));
//                    }
//                    return;
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private Long extractId(String requestURI) {
//        String[] parts = requestURI.split("/");
//        return Long.parseLong(parts[parts.length - 1]);
//    }
//}
