package com.soen345.ticketreservation.unit.model;

import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .password("encoded_password")
                .role(UserRole.CUSTOMER)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals("encoded_password", user.getPassword());
        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testDefaultRole() {
        User user = User.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .password("password")
                .build();

        assertEquals(UserRole.CUSTOMER, user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(2L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setPhone("9876543210");
        user.setPassword("secret");
        user.setRole(UserRole.ADMIN);

        assertEquals(2L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("9876543210", user.getPhone());
        assertEquals("secret", user.getPassword());
        assertEquals(UserRole.ADMIN, user.getRole());
    }
}
