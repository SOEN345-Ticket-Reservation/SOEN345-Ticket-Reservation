package com.soen345.ticketreservation.integration.repository;

import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = User.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    void findByEmail_Found() {
        Optional<User> found = userRepository.findByEmail("john@example.com");
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void findByEmail_NotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    void findByPhone_Found() {
        Optional<User> found = userRepository.findByPhone("1234567890");
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void existsByEmail_True() {
        assertTrue(userRepository.existsByEmail("john@example.com"));
    }

    @Test
    void existsByEmail_False() {
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void existsByPhone_True() {
        assertTrue(userRepository.existsByPhone("1234567890"));
    }
}
