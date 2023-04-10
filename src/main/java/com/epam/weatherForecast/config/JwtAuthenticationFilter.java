package com.epam.weatherForecast.config;

import com.epam.weatherForecast.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, @Lazy UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (isAuthHeaderValid(authHeader)) {
            String jwtToken = authHeader.substring(TOKEN_PREFIX.length());
            String userLogin = jwtService.extractUserLogin(jwtToken);
            updateContext(userLogin, jwtToken, request);
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAuthHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith(TOKEN_PREFIX);
    }

    private void updateContext(String userLogin, String jwtToken,HttpServletRequest request ){
        SecurityContext context = SecurityContextHolder.getContext();
        if(userLogin !=null && context.getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin);
            if(jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = getAuthToken(userDetails);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            }
        }
    }
    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails userDetails){
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
//    TODO refactor updateContext();
}