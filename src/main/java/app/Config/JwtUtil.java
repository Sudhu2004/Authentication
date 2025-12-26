package app.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Generate a secure key - in production, store this in environment variables
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "mySecretKeyForJWTTokenGeneration12345678901234567890".getBytes()
    ); // Must be at least 256 bits (32 bytes)

    private final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 10; // 10 hours

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()  // Changed from parserBuilder()
                .verifyWith(SECRET_KEY)  // Changed from setSigningKey()
                .build()
                .parseSignedClaims(token)  // Changed from parseClaimsJws()
                .getPayload();  // Changed from getBody()
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate token for user
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)  // Changed from setClaims()
                .subject(subject)  // Changed from setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))  // Changed from setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))  // Changed from setExpiration()
                .signWith(SECRET_KEY)  // Simplified - no need to specify algorithm
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}
