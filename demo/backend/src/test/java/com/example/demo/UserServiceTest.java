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
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.service.UserServiceImpl;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldReturnAllUsersFromRepository() {
       List<User> expectedUsers = Arrays.asList(new User(1L, "Test User", "test@example.com", "password123", "Test", "User"));
       when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getAllUsers();

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers.get(0).getUsername(), actualUsers.get(0).getUsername());
    }

    @Test
    public void shouldCreatenewUser() {
        User newUser = new User(null, "alex_dev", "alex@example.com", "pass", "Alex", "Pop");
        User savedUser = new User(1L, "alex_dev", "alex@example.com", "pass", "Alex", "Pop");

        when(userRepository.save(any(User.class))).thenReturn(savedUser); 

        User result = userService.createUser(newUser);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
    }

    @Test
    public void shouldFindUserById() {
        User mockUser = new User(1L, "maria99", "maria@example.com", "pass", "Maria", "Ion");

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserById(1L);
        assertEquals(mockUser.getId(), result.get().getId());
        assertEquals(mockUser.getUsername(), result.get().getUsername());
    }

    @Test
    public void shouldUpdateExistingUser() {
        User existingUser = new User(1L, "vechi_user", "vechi@email.com", "pass", "Vasile", "Pop");

        User noileDate = new User(null, "nume_nou", "nou@email.com", "pass", "Vasile", "Pop");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateUser(1L, noileDate);

        assertEquals(existingUser.getEmail(), result.getEmail());
        assertEquals(noileDate.getUsername(), result.getUsername());
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        User noileDate = new User(null, "nume_nou", "nou@email.com", "pass", "Vasile", "Pop");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L,noileDate);
        });

        assertEquals("User not found with id", exception.getMessage());

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

        List<User> result = userService.searchTop10Users("user");

        assertEquals(2, result.size());
        assertEquals("a_user", result.get(0).getUsername());
    }
    
}
