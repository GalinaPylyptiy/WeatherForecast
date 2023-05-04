package com.epam.weatherForecast.service.impl;

import com.epam.weatherForecast.model.AuthResponse;
import com.epam.weatherForecast.entity.Role;
import com.epam.weatherForecast.entity.User;
import com.epam.weatherForecast.repository.RoleRepository;
import com.epam.weatherForecast.repository.UserRepository;
import com.epam.weatherForecast.service.JwtService;
import com.epam.weatherForecast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse addUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Role role = roleRepository.getRoleByRoleName("USER");
        user.setPassword(encodedPassword);
        user.setRoles(List.of(role));
        userRepository.save(user);
        String jwtToken =jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void addRoleToUser(User user, Role role) {
        user.getRoles().add(role);
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public AuthResponse authenticate(User user) {
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        String jwtToken =jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

}
