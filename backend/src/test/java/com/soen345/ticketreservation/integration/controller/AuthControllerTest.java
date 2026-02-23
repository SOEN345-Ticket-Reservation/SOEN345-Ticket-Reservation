package com.soen345.ticketreservation.integration.controller;

import tools.jackson.databind.ObjectMapper;
import com.soen345.ticketreservation.dto.request.LoginRequest;
import com.soen345.ticketreservation.dto.request.RegisterRequest;
import com.soen345.ticketreservation.dto.response.UserResponse;
import com.soen345.ticketreservation.model.enums.UserRole;
import com.soen345.ticketreservation.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void register_Success() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(UserRole.CUSTOMER)
                .build();

        when(userService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void register_InvalidEmail_BadRequest() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("John Doe")
                .email("invalid-email")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_Success() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .emailOrPhone("john@example.com")
                .password("password123")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .role(UserRole.CUSTOMER)
                .build();

        when(userService.authenticate(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}
