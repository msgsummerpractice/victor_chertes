package com.example.demo.service;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.PatchUserRequestDTO;
import com.example.demo.dto.UpdateUserRequestDTO;
import com.example.demo.dto.UserRequestDTO;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO request);
    Optional<UserResponseDTO> getUserById(Long id);
    Page<UserResponseDTO> getAllUsers(int page, int size);
    UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request);
    void deleteUser(Long id);
    Optional<UserResponseDTO> getUserByUsername(String username);
    Optional<UserResponseDTO> getUserByEmail(String email);
    List<UserResponseDTO> searchTop10Users(String keyword);
    long countTotalUsers();
    UserResponseDTO patchUser(Long id, PatchUserRequestDTO updates);
}
