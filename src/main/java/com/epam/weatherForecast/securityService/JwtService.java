package com.epam.weatherForecast.securityService;

import com.epam.weatherForecast.entity.User;

import java.util.Map;

public interface JwtService {

    String extractUserLogin(String jwtToken);

    String generateToken(Map<String, Object> extraClaims, User user);

    String generateToken(User user);

    void validateToken(String jwtToken);
}
