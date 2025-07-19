package com.suaistuds.monitoringequipment.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.suaistuds.monitoringequipment.payload.ApiResponse;
import com.suaistuds.monitoringequipment.payload.JwtAuthenticationResponse;
import com.suaistuds.monitoringequipment.payload.user.LoginRequest;
import com.suaistuds.monitoringequipment.payload.user.SignUpRequest;
import com.suaistuds.monitoringequipment.security.JwtTokenProvider;
import com.suaistuds.monitoringequipment.service.UserService;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 *
 * <p>Предоставляет REST API для:
 * <ul>
 *   <li>Аутентификации пользователей (вход в систему)</li>
 *   <li>Регистрации новых пользователей</li>
 * </ul>
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Генерация JWT токена при успешной аутентификации</li>
 *   <li>Валидация входных данных</li>
 *   <li>Интеграция с Spring Security</li>
 * </ul>
 *
 * <p>Используемые компоненты:
 * <ul>
 *   <li>{@link AuthenticationManager} - для аутентификации пользователей</li>
 *   <li>{@link JwtTokenProvider} - для генерации JWT токенов</li>
 *   <li>{@link UserService} - для регистрации новых пользователей</li>
 * </ul>
 *
 * @since 2025-07-13
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private UserService userService;

    /**
     * Аутентифицирует пользователя и возвращает JWT токен.
     *
     * @param loginRequest запрос на аутентификацию ({@link LoginRequest})
     * @return ответ с JWT токеном ({@link JwtAuthenticationResponse})
     * @throws org.springframework.security.core.AuthenticationException если аутентификация не удалась
     * @see LoginRequest
     * @see JwtAuthenticationResponse
     */
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

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param signUpRequest запрос на регистрацию ({@link SignUpRequest})
     * @return ответ с результатом операции ({@link ApiResponse})
     * @throws com.suaistuds.monitoringequipment.exception.BadRequestException если имя пользователя или email уже заняты
     * @see SignUpRequest
     * @see ApiResponse
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(
            @Valid @RequestBody SignUpRequest signUpRequest) {
        userService.create(signUpRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully"));
    }
}
