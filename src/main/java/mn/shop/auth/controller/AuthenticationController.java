package mn.shop.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.shop.auth.model.AuthenticationRequest;
import mn.shop.auth.service.JwtUtils;
import mn.shop.auth.service.TokenBlacklistService;
import mn.shop.user.model.User;
import mn.shop.user.service.UserService;
import mn.shop.utils.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;


//
//    @CrossOrigin(origins = "*")
//    @PostMapping("login")
//    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        User user = userService.findUserByUserName(request.getUsername());
//        if (user != null) {
//            return ResponseEntity.ok(jwtUtils.generateToken(user));
//        }
//        else throw new ValidationException("Нэвтрэх нэр эсвэл нууц үг буруу байна");
//    }


    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return authorizationHeader;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request,
                                        @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("User " + request.getUsername() + " is login.");
        String existingToken = extractTokenFromHeader(authorizationHeader);
        boolean isTokenValid = !jwtUtils.isTokenExpired(existingToken);
        User user = userService.findUserByUserName(request.getUsername());
        if (Objects.isNull(user)) throw new ValidationException("Invalid username or password");
        userService.setLoginTime(user);
        if (isTokenValid) {
            return ResponseEntity.ok(existingToken);
        } else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String newToken = jwtUtils.generateToken(user);
            return ResponseEntity.ok(newToken);
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            log.info("User " + username + " is logging out.");
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

//        String token = extractTokenFromRequest(request);

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("successfully logout");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/logout-bl")
    public ResponseEntity<String> logoutWithBlackList(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String token = extractTokenFromRequest(request);  // Implement this method to extract the token
            tokenBlacklistService.addToBlacklist(token);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            log.info("User " + authentication.getName() + " is logging out.");
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }

}
