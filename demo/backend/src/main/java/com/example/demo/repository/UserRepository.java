package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findTop10ByUsernameContainingIgnoreCaseOrderByUsernameAsc(String keyword);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
}
