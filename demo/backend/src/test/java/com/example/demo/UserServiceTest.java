package com.example.demo;

import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.service.UserServiceImpl;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldReturnAllUsersFromRepository() {
       List<User> expectedUsers = Arrays.asList(new User(1L, "Test User"));
       when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(1, actualUsers.size());
        assertEquals("Test User", actualUsers.get(0).getUsername());
    }
    
}
