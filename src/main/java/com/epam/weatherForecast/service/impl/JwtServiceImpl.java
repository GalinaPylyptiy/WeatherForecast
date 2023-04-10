package com.epam.weatherForecast.service.impl;
import com.epam.weatherForecast.entity.User;
import com.epam.weatherForecast.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@PropertySource("classpath:value.properties")
public class JwtServiceImpl implements JwtService {

    @Value("${secretKey}")
    private  String SECRET_KEY;

    @Override
    public String extractUserLogin(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public String generateToken(User user){
      return   generateToken(new HashMap<>(), user);
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
    public boolean isTokenValid(String jwtToken, UserDetails user){
        String login = extractUserLogin(jwtToken);
        return user.getUsername().equals(login) && !isTokenExpired(jwtToken);
    }

    private <T>  T extractClaim(String jwtToken, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String jwtToken){
        Date exp = extractClaim(jwtToken, Claims::getExpiration);
        return exp.before(new Date());
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts.parserBuilder()
                .setSigningKey(getSighInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSighInKey() {
        byte[]keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
