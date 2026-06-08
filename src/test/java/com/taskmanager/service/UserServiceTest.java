package com.taskmanager.service;

import com.taskmanager.model.User;
import com.taskmanager.model.User.Role;
import com.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User("alice", "alice@example.com", "pass123", Role.USER);
        mockUser.setId(1L);
    }

    @Test
    void testCreateUser_success() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.createUser("alice", "alice@example.com", "pass123", Role.USER);

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        assertEquals(Role.USER, result.getRole());
    }

    @Test
    void testCreateUser_duplicateUsername_throwsException() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userService.createUser("alice", "other@example.com", "pass", Role.USER));
    }

    @Test
    void testUpdateUserRole_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        mockUser.setRole(Role.ADMIN);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User updated = userService.updateUserRole(1L, Role.ADMIN);

        assertEquals(Role.ADMIN, updated.getRole());
    }

    @Test
    void testDeleteUser_notFound_throwsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));
    }
}
