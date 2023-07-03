package com.epam.weatherForecast.securityService.impl;

import com.epam.weatherForecast.entity.User;
import com.epam.weatherForecast.exception.InvalidJwtTokenException;
import com.epam.weatherForecast.securityService.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@PropertySource("classpath:value.properties")
public class JwtServiceImpl implements JwtService {

    @Value("${secretKey}")
    private String SECRET_KEY;

    @Override
    public String extractUserLogin(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, User user) {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSighInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public void validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSighInKey())
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (Exception ex) {
            log.error("Token is not valid: {}", ex.getMessage());
            throw new InvalidJwtTokenException("JWT token is invalid");
        }
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSighInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSighInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
