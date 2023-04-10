package com.epam.weatherForecast.service;

import com.epam.weatherForecast.model.AuthResponse;
import com.epam.weatherForecast.entity.Role;
import com.epam.weatherForecast.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    AuthResponse addUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByLogin(String login);
    void addRoleToUser(User user, Role role);
    Collection<User> getAll();
    AuthResponse authenticate(User user);



}
