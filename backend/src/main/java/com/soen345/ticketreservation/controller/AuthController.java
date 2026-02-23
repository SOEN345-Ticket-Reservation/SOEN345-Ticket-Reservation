package com.soen345.ticketreservation.controller;

import com.soen345.ticketreservation.dto.request.LoginRequest;
import com.soen345.ticketreservation.dto.request.RegisterRequest;
import com.soen345.ticketreservation.dto.response.UserResponse;
import com.soen345.ticketreservation.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        UserResponse response = userService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
