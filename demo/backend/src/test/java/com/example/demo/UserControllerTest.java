package com.example.demo;

import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.demo.config.AppSettings;
import com.example.demo.controller.UserController;
import com.example.demo.dto.PatchUserRequestDTO;
import com.example.demo.dto.UpdateUserRequestDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AppSettings appSettings;

    @Test
    public void shouldReturnUsersListAndStatusOk() throws Exception {
        List<UserResponseDTO> mockUsers = Arrays.asList(
                new UserResponseDTO(1L, "Ion Popescu", "ion@example.com", "Ion", "Popescu"),
                new UserResponseDTO(2L, "user2", "user2@example.com", "User", "Two"));

        Page<UserResponseDTO> mockPage = new PageImpl<>(mockUsers);

        when(userService.getAllUsers(0, 10)).thenReturn(mockPage);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].username").value("Ion Popescu"))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }

    @Test
    public void shouldReturnUserById() throws Exception {
        UserResponseDTO mockUser = new UserResponseDTO(1L, "maria99", "maria@example.com", "Maria", "Ion");

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("maria99"))
                .andExpect(jsonPath("$.email").value("maria@example.com"));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("alex_dev", "alex@example.com", "password123", "Alex", "Pop");

        UserResponseDTO responseDTO = new UserResponseDTO(1L, "alex_dev", "alex@example.com", "Alex", "Pop");

        when(userService.createUser(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("alex_dev"));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        UpdateUserRequestDTO updateRequest = new UpdateUserRequestDTO("alex_dev2", "alex2@example.com", "newpass123",
                "Alex", "Pop");
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "alex_dev2", "alex2@example.com", "Alex", "Pop");

        when(userService.updateUser(1L, updateRequest)).thenReturn(responseDTO);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alex_dev2"))
                .andExpect(jsonPath("$.email").value("alex2@example.com"));
    }

    @Test
    public void shouldPatchUser() throws Exception {
        PatchUserRequestDTO patchRequest = new PatchUserRequestDTO();
        patchRequest.setPassword("brandNewPassword123");

        UserResponseDTO responseDTO = new UserResponseDTO(1L, "alex_dev2", "alex2@example.com", "Alex", "Pop");

        when(userService.patchUser(1L, patchRequest)).thenReturn(responseDTO);

        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alex_dev2"));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        UserResponseDTO mockUser = new UserResponseDTO(1L, "delete_me", "delete@example.com", "Test", "User");
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(delete("/users/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
