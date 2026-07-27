package com.example.demo;

import com.example.demo.service.UserService;
import com.example.demo.config.AppSettings;
import com.example.demo.controller.UserController;
import com.example.demo.dto.UserResponse;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AppSettings appSettings;

    
    @Test
    public void shouldReturnUsersListAndStatusOk() throws Exception {
        List<UserResponse> mockUsers = Arrays.asList(
                new UserResponse(1L, "Ion Popescu", "ion@example.com", "Ion", "Popescu"), 
                new UserResponse(2L, "user2", "user2@example.com", "User", "Two")
        );

        Page<UserResponse> mockPage = new PageImpl<>(mockUsers);

        when(userService.getAllUsers(anyInt(),anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk()) 
                .andExpect(content().contentType("application/json")) 
                .andExpect(jsonPath("$.content[0].username").value("Ion Popescu")) 
                .andExpect(jsonPath("$.content[1].id").value(2));
    }


}
