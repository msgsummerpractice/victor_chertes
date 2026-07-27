package com.example.demo;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.demo.service.UserServiceImpl;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.UserMapper;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.dto.UserRequest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private UserMapper userMapper;

    @Test
    public void shouldReturnAllUsersFromRepository() {
       List<User> expectedUsers = Arrays.asList(new User(1L, "Test User", "test@example.com", "pass",  "Test", "User"));
       Page<User> expectedUserPage = new PageImpl<>(expectedUsers);


       when(userRepository.findAll(any(Pageable.class))).thenReturn(expectedUserPage);

        Page<UserResponse> actualUsers = userService.getAllUsers(0,10);

        assertEquals(expectedUsers.size(), actualUsers.getContent().size());
        assertEquals(expectedUsers.get(0).getUsername(), actualUsers.getContent().get(0).getUsername());
    }

    @Test
    public void shouldCreatenewUser() {
        UserRequest newUser = new UserRequest(null, "alex_dev", "alex@example.com", "Alex", "Pop");
        User savedUser = new User(1L, "alex_dev", "alex@example.com", "pass", "Alex", "Pop");

        when(userRepository.save(any(User.class))).thenReturn(savedUser); 

        UserResponse result = userService.createUser(newUser);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
    }

    @Test
    public void shouldFindUserById() {
        User mockUser = new User(1L, "maria99", "maria@example.com", "pass", "Maria", "Ion");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<UserResponse> result = userService.getUserById(1L);
        assertEquals(mockUser.getId(), result.get().getId());
        assertEquals(mockUser.getUsername(), result.get().getUsername());
    }

    @Test
    public void shouldUpdateExistingUser() {
        User existingUser = new User(1L, "vechi_user", "vechi@email.com", "pass", "Vasile", "Pop");

        UpdateUserRequest noileDate = new UpdateUserRequest( "nume_nou", "nou@email.com", "pass", "Vasile", "Pop");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponse result = userService.updateUser(1L, noileDate);

        assertEquals(existingUser.getEmail(), result.getEmail());
        assertEquals(noileDate.getUsername(), result.getUsername());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        UpdateUserRequest noileDate = new UpdateUserRequest( "nume_nou", "nou@email.com", "pass", "Vasile", "Pop");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L,noileDate);
        });

        assertEquals("User not found with id 1", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        
    }

    @Test
    public void shouldDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldReturnTop10Users() {
        List<User> dummyList = Arrays.asList(
            new User(1L, "a_user", "a@test.com", "pass", "A", "A"),
            new User(2L, "b_user", "b@test.com", "pass", "B", "B")
        );

        when(userRepository.findTop10ByUsernameContainingIgnoreCaseOrderByUsernameAsc("user")).thenReturn(dummyList);

        List<UserResponse> result = userService.searchTop10Users("user");

        assertEquals(2, result.size());
        assertEquals("a_user", result.get(0).getUsername());
    }
    
}
