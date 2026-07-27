package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.mapper.UserMapper;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<UserResponse> getAllUsers(int page, int size) {
        logger.info("Fetching all users from the repository");
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toResponse);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + request.getUsername() + "' is already taken.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email '" + request.getEmail() + "' is already in use.");
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id) 
                .map(userMapper::toResponse);

    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        return userRepository.findById(id).map(existingUser -> {
            Optional<User> userWithSameUsername = userRepository.findByUsername(request.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                throw new RuntimeException("Username '" + request.getUsername() + "' is already taken by another user.");
            }

            Optional<User> userWithSameEmail = userRepository.findByEmail(request.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                throw new RuntimeException("Email '" + request.getEmail() + "' is already in use by another user.");
            }

            userMapper.updateEntityFromDto(request,existingUser);
            User updatedUser = userRepository.save(existingUser);
            
            return userMapper.toResponse(updatedUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(userMapper::toResponse);
    }

    @Override
    public List<UserResponse> searchTop10Users(String keyword) {
        return userRepository.findTop10ByUsernameContainingIgnoreCaseOrderByUsernameAsc(keyword)
            .stream()
            .map(userMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public long countTotalUsers() {
        return userRepository.countUsers();
    }

    @Override
    public UserResponse patchUser(Long id, Map<String, Object> updates) {
        return userRepository.findById(id).map(existingUser -> {
            
            if (updates.containsKey("username")) {
                String newUsername = (String) updates.get("username");
                Optional<User> userWithSameUsername = userRepository.findByUsername(newUsername);
                if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                    throw new RuntimeException("Username '" + newUsername + "' is already taken.");
                }
                existingUser.setUsername(newUsername);
            }
            
            if (updates.containsKey("email")) {
                String newEmail = (String) updates.get("email");
                Optional<User> userWithSameEmail = userRepository.findByEmail(newEmail);
                if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                    throw new RuntimeException("Email '" + newEmail + "' is already in use.");
                }
                existingUser.setEmail(newEmail);
            }
            
            if (updates.containsKey("password")) existingUser.setPassword((String) updates.get("password"));
            if (updates.containsKey("firstname")) existingUser.setFirstname((String) updates.get("firstname"));
            if (updates.containsKey("lastname")) existingUser.setLastname((String) updates.get("lastname"));
            
            User updatedUser = userRepository.save(existingUser);
            return userMapper.toResponse(updatedUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

}
