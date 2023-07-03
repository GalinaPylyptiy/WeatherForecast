package com.epam.weatherForecast.controller;

import com.epam.weatherForecast.model.AuthResponse;
import com.epam.weatherForecast.entity.User;
import com.epam.weatherForecast.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user){
        return ResponseEntity.ok(userService.addUser(user));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody User user){
       return ResponseEntity.ok(userService.authenticate(user));
    }

}

