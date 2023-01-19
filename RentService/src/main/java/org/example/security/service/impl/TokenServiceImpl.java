package org.example.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.security.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${oauth.jwt.secret}")
    private String jwtSecret;

    @Override
    public String generate(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public Claims parseToken(String jwt) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims;
    }

    @Override
    public Long getCompanyId(String jwt) {
        String token = jwt.split(" ")[1];
        Claims claims = parseToken(token);
        return claims.get("company_id", Long.class);
    }

    public Long getUserId(String jwt) {
        String token = jwt.split(" ")[1];
        Claims claims = parseToken(token);
        return claims.get("id", Long.class);
    }

    public Pair<String, Long> getUserInfo(String jwt) {
        String token = jwt.split(" ")[1];
        Claims claims = parseToken(token);
        if(claims.get("role", String.class).equals("MANAGER"))
            return Pair.of("MANAGER", claims.get("company_id", Long.class));
        return Pair.of(claims.get("role", String.class), claims.get("id", Long.class));
    }


}
