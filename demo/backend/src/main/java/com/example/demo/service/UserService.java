package com.example.demo.service;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;
import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponse createUser(UserRequest request);
    Optional<UserResponse> getUserById(Long id);
    Page<UserResponse> getAllUsers(int page, int size);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void deleteUser(Long id);
    Optional<UserResponse> getUserByUsername(String username);
    Optional<UserResponse> getUserByEmail(String email);
    List<UserResponse> searchTop10Users(String keyword);
    long countTotalUsers();
    UserResponse patchUser(Long id, Map<String, Object> updates);
}
