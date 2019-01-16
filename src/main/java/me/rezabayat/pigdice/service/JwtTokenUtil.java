package me.rezabayat.pigdice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.rezabayat.pigdice.exception.AuthorizeException;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtTokenUtil {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).signWith(key).compact();
    }

    public String getUsername(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return claimsJws.getBody().getSubject();
        } catch (Exception e) {
            throw new AuthorizeException("Authorization failed");
        }
    }
}
