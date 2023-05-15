package com.epam.weatherForecast.service;

import com.epam.weatherForecast.model.AuthResponse;
import com.epam.weatherForecast.entity.Role;
import com.epam.weatherForecast.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    AuthResponse addUser(User user);
    Optional<User> findUserById(Long id);
    Optional<User> findUserByLogin(String login);
    void addRoleToUser(User user, Role role);
    Collection<User> findAll();
    AuthResponse authenticate(User user);


}
