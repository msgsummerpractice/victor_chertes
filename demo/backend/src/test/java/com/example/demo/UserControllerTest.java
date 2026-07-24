package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;

    
    @Test
    public void shouldReturnUsersListAndStatusOk() throws Exception {
        List<User> mockUsers = Arrays.asList(
                new User(1L, "Ion Popescu", "ion@example.com", "password123", "Ion", "Popescu"), 
                new User(2L, "user2", "user2@example.com", "password456", "User", "Two")
        );

        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk()) 
                .andExpect(content().contentType("application/json")) 
                .andExpect(jsonPath("$[0].username").value("Ion Popescu")) 
                .andExpect(jsonPath("$[1].id").value(2));
    }


}
