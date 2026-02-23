package com.soen345.ticketreservation.acceptance;

import tools.jackson.databind.ObjectMapper;
import com.soen345.ticketreservation.dto.request.LoginRequest;
import com.soen345.ticketreservation.dto.request.RegisterRequest;
import com.soen345.ticketreservation.model.User;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserRegistrationAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void fullFlow_Register_Login_BrowseEvents() throws Exception {
        // Step 1: Register a new user
        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Alice Smith")
                .email("alice@example.com")
                .phone("5551234567")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        // Step 2: Login with the registered user
        LoginRequest loginRequest = LoginRequest.builder()
                .emailOrPhone("alice@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        // Step 3: Browse events (public endpoint)
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk());
    }

    @Test
    void register_DuplicateEmail_Fails() throws Exception {
        // Pre-create a user
        userRepository.save(User.builder()
                .name("Existing User")
                .email("existing@example.com")
                .password(passwordEncoder.encode("password"))
                .role(UserRole.CUSTOMER)
                .build());

        // Try registering with same email
        RegisterRequest request = RegisterRequest.builder()
                .name("New User")
                .email("existing@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
