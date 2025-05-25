package com.example.DemoSecurity.service;

import com.example.DemoSecurity.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey=null;



    public String generateToken(User user) {
        Map<String,Object> claims=new HashMap<>();

        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUserName())
                .issuer("NDP")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000  * 60 * 10)) // 10 minutes expiration
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte[] decode = Decoders.BASE64.decode(getSecretKey()); // Decode the base64 encoded secret key
        return Keys.hmacShaKeyFor(decode); // Using HMAC SHA for signing the JWT
    }


    public String getSecretKey(){
        return secretKey="b37bda473cd0ff0a376ad6b342524459e33e124622b254a8b6fc532810f646c8390221b193b2f22e501d2c01758a0894c392a2929fd7a7a6930fa28886fdceb5";
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject); // Extract the subject (username) from the JWT token
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) { // Generic method to extract claims from the JWT token
        final Claims claims = extractClaims(token); // Extract claims from the token using the extractClaims method
        return claimResolver.apply(claims); // Apply the claim resolver function to the extracted claims
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser() // Create a JWT parser
                .verifyWith(generateKey()) // Verify the token with the generated key
                .build() // Build the parser
                .parseSignedClaims(token) // Parse the signed claims from the token
                .getPayload(); // Extract the payload (claims) from the parsed token

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract the username from the token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Check if the username matches and the token is not expired

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if the expiration date of the token is before the current date
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration); // Extract the expiration date from the token claims
    }
}
