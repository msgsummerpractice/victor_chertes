package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.mapper.UserMapper;
import com.example.demo.dto.UpdateUserRequestDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.PatchUserRequestDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(int page, int size) {
        logger.info("Fetching all users from the repository");
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(userMapper::toResponse);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + request.getUsername() + "' is already taken.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email '" + request.getEmail() + "' is already in use.");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse);

    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {
        return userRepository.findById(id).map(existingUser -> {
            Optional<User> userWithSameUsername = userRepository.findByUsername(request.getUsername());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                throw new RuntimeException(
                        "Username '" + request.getUsername() + "' is already taken by another user.");
            }

            Optional<User> userWithSameEmail = userRepository.findByEmail(request.getEmail());
            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                throw new RuntimeException("Email '" + request.getEmail() + "' is already in use by another user.");
            }

            userMapper.updateEntityFromDto(request, existingUser);

            if (StringUtils.hasText(request.getPassword())) {
                 existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            User updatedUser = userRepository.save(existingUser);

            return userMapper.toResponse(updatedUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserResponseDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse);
    }

    @Override
    public Optional<UserResponseDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toResponse);
    }

    @Override
    public List<UserResponseDTO> searchTop10Users(String keyword) {
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
    public UserResponseDTO patchUser(Long id, PatchUserRequestDTO updates) {
        return userRepository.findById(id).map(existingUser -> {
            
            if (StringUtils.hasText(updates.getUsername())) {
                Optional<User> userWithSameUsername = userRepository.findByUsername(updates.getUsername());
                if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                    throw new RuntimeException("Username '" + updates.getUsername() + "' is already taken.");
                }
                existingUser.setUsername(updates.getUsername());
            }
            
            if (StringUtils.hasText(updates.getEmail())) {
                Optional<User> userWithSameEmail = userRepository.findByEmail(updates.getEmail());
                if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {
                    throw new RuntimeException("Email '" + updates.getEmail() + "' is already in use.");
                }
                existingUser.setEmail(updates.getEmail());
            }
            
            if (StringUtils.hasText(updates.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(updates.getPassword()));
            }
            if (StringUtils.hasText(updates.getFirstname())) {
                existingUser.setFirstName(updates.getFirstname());
            }
            if (StringUtils.hasText(updates.getLastname())) {
                existingUser.setLastName(updates.getLastname());
            }
            
            User updatedUser = userRepository.save(existingUser);
            return userMapper.toResponse(updatedUser);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

}
