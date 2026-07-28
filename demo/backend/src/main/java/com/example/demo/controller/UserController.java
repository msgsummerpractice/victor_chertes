package com.example.demo.controller;

import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import com.example.demo.config.AppSettings;
import com.example.demo.dto.PatchUserRequestDTO;
import com.example.demo.dto.UpdateUserRequestDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AppSettings appSettings;

    @Value("${app.custom.welcome.message}")
    private String welcomeMessage;

    public UserController(UserService userService, AppSettings appSettings) {
        this.userService = userService;
        this.appSettings = appSettings;
    }

    @GetMapping("/message")
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    @GetMapping("/version")
    public String getVersion() {
        return appSettings.getVersion();
    }

    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ApiResponse(responseCode = "200", description = "When a page of users is returned")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResponseDTO> users = userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    @ApiResponse(responseCode = "200", description = "When a user is returned")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "When a user is saved")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "When a user is updated")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO userDetails) {
        UserResponseDTO updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "When a user is patched")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable Long id,
            @Valid @RequestBody PatchUserRequestDTO request) {
        UserResponseDTO patchedUser = userService.patchUser(id, request);
        return ResponseEntity.ok(patchedUser);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "When a user is deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
