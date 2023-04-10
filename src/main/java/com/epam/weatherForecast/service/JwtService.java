package com.epam.weatherForecast.service;

import com.epam.weatherForecast.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {

    String extractUserLogin(String jwtToken);
    String generateToken(Map<String, Object> extraClaims, User user);
    String generateToken(User user);
    boolean isTokenValid(String jwtToken, UserDetails user);
}
