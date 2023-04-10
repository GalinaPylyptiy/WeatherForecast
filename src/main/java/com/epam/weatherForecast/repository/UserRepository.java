package com.epam.weatherForecast.repository;

import com.epam.weatherForecast.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();
    Optional<User> findById(Long id);
    Optional <User> findByLogin(String login);

}
