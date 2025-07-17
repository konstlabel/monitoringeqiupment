package com.suaistuds.monitoringequipment.controller;

import com.suaistuds.monitoringequipment.payload.PagedResponse;
import com.suaistuds.monitoringequipment.payload.user.*;
import com.suaistuds.monitoringequipment.service.UserService;
import com.suaistuds.monitoringequipment.security.CurrentUser;
import com.suaistuds.monitoringequipment.security.UserPrincipal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Основные операции
    @GetMapping
    public PagedResponse<UserProfile> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        return userService.getAll(page, size);
    }

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/id/{userId}")
    public UserProfile getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/username/{username}")
    public UserProfile getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/email/{email}")
    public UserProfile getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{username}/profile")
    public UserProfile getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    // Проверка доступности
    @GetMapping("/check/username")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam String username) {
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("/check/email")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam String email) {
        return userService.checkEmailAvailability(email);
    }

    // CRUD операции
    @PostMapping
    public ResponseEntity<UserSummary> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserSummary userSummary = userService.create(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSummary);
    }

    @PutMapping("/{username}")
    public UserSummary updateUser(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateRequest updateRequest,
            @CurrentUser UserPrincipal currentUser) {
        return userService.update(username, updateRequest, currentUser);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable String username,
            @CurrentUser UserPrincipal currentUser) {
        userService.delete(username, currentUser);
    }

    // Управление ролями
    @PutMapping("/{username}/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void grantAdminRole(@PathVariable String username) {
        userService.giveAdmin(username);
    }

    @DeleteMapping("/{username}/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAdminRole(@PathVariable String username) {
        userService.removeAdmin(username);
    }

    @PutMapping("/{username}/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void grantStudioRole(@PathVariable String username) {
        userService.giveStudio(username);
    }

    @DeleteMapping("/{username}/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeStudioRole(@PathVariable String username) {
        userService.removeStudio(username);
    }

    // Фильтрация
    @GetMapping("/role/{roleId}")
    public PagedResponse<UserProfile> getUsersByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return userService.getUsersByRole(roleId, page, size);
    }
}