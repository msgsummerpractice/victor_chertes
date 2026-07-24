package com.example.demo.service;
import com.example.demo.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    List<User> searchTop10Users(String keyword);
    long countTotalUsers();
}
