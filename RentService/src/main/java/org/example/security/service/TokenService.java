package org.example.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.data.util.Pair;

public interface TokenService {

    String generate(Claims claims);

    Claims parseToken(String jwt);

    Long getCompanyId(String jwt);

    Long getUserId(String jwt);

    Pair<String, Long> getUserInfo(String jwt);
}

