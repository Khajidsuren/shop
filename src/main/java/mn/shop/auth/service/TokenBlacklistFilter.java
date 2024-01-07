package mn.shop.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenBlacklistFilter extends GenericFilterBean {

    private final TokenBlacklistService tokenBlacklistService;

    public TokenBlacklistFilter(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String token = extractTokenFromRequest((HttpServletRequest) request);
            if (token != null && tokenBlacklistService.isTokenBlacklisted(token)) {
                // Token is blacklisted, reject the request
                // You may choose to return a 403 Forbidden response or handle it as needed
                response.getWriter().write("Token is blacklisted");
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Extract the token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " is 7 characters long
        }
        return null;
    }
}
