package com.suaistuds.monitoringequipment.controller;

import com.suaistuds.monitoringequipment.payload.ApiResponse;
import com.suaistuds.monitoringequipment.payload.JwtAuthenticationResponse;
import com.suaistuds.monitoringequipment.payload.user.LoginRequest;
import com.suaistuds.monitoringequipment.payload.user.SignUpRequest;
import com.suaistuds.monitoringequipment.security.JwtTokenProvider;
import com.suaistuds.monitoringequipment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Передаём весь Authentication
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(
            @Valid @RequestBody SignUpRequest signUpRequest) {
        userService.create(signUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully"));
    }
}
