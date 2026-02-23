package com.soen345.ticketreservation.unit.service;

import com.soen345.ticketreservation.dto.request.LoginRequest;
import com.soen345.ticketreservation.dto.request.RegisterRequest;
import com.soen345.ticketreservation.dto.response.UserResponse;
import com.soen345.ticketreservation.exception.DuplicateRegistrationException;
import com.soen345.ticketreservation.exception.ResourceNotFoundException;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.UserRepository;
import com.soen345.ticketreservation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build();

        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("password123")
                .build();

        loginRequest = LoginRequest.builder()
                .emailOrPhone("john@example.com")
                .password("password123")
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhone(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals(UserRole.CUSTOMER, response.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateRegistrationException.class,
                () -> userService.register(registerRequest));
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);

        UserResponse response = userService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhone(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.authenticate(loginRequest));
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.authenticate(loginRequest));
    }
}
