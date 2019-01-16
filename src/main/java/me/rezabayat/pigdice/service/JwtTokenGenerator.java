package me.rezabayat.pigdice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtTokenGenerator {

    public String generateToken(String username){
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Jwts.builder().setSubject(username).signWith(key).compact();
    }
}
