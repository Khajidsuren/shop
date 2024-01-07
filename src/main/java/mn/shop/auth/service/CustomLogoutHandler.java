package mn.shop.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Perform any server-side action related to the token during logout
        // For example, you might want to invalidate the token on the server side

        // Retrieve the token from the request, e.g., from a header
        String token = extractTokenFromRequest(request);

        // Perform actions with the token, e.g., invalidate it on the server side

        // Clear the security context
        SecurityContextHolder.clearContext();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Implement logic to extract the token from the request
        // This could involve reading headers, cookies, etc.
        // For example, if the token is in the Authorization header:
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
