package mn.shop.auth.service;
//
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.sql.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Function;
//
//@Component
//public class JwtUtils {
//
//    private String jwtSiginKey = "secret";
//
//    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//
//
//    private final byte[] signingKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
//
//
//
//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return (Date) extractClaim(token, Claims::getExpiration);
//    }
//
//    public boolean hasClaim(String token, String claimName) {
//        final Claims claims = extractAllClaims(token);
//        return claims.get(claimName) != null;
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody();
//    }
//
//    public Boolean isTokenExpired(String token) {
//        try {
//            return extractExpiration(token).before(new java.util.Date());
//        } catch (ExpiredJwtException | UnsupportedJwtException ex) {
//            return true;
//        }
//    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Objects> claims = new HashMap<>();
//        return createToken(claims, userDetails);
//    }
//
//    public String generateToken(UserDetails userDetails, Map<String, Objects> claims) {
//        return createToken(claims, userDetails);
//
//    }
//
//    public String createToken(Map<String, Objects> claims, UserDetails userDetails) {
//        return Jwts.builder().setClaims(claims)
//                .setSubject(userDetails.getUsername())
//                .claim("authorities", userDetails.getAuthorities())
//                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
//                .setExpiration(new java.util.Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
//                .signWith(secretKey, SignatureAlgorithm.HS256).compact();
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUserName(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//
//
//}


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import mn.shop.user.model.User;
import mn.shop.user.model.UserRole;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private static final String CLAIM_KEY_ROLES = "roles";
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), userDetails.getRole());
    }

    private String createToken(Map<String, Object> claims, String subject, UserRole role) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .claim(CLAIM_KEY_ROLES, role.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 min expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, User userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            // Parse the token and verify the signature
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Check additional conditions if needed
            // For example, you might check the issuer, audience, or other claims

            // The token is considered valid
            return true;
        } catch (Exception e) {
            // The token validation failed
            return false;
        }
    }

    interface ClaimsResolver<T> {
        T apply(Claims claims);
    }
}
