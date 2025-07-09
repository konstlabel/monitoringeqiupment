package com.suaistuds.monitoringeqiupment.controller;

import com.suaistuds.monitoringeqiupment.payload.user.UserIdentityAvailability;
import com.suaistuds.monitoringeqiupment.payload.user.UserProfile;
import com.suaistuds.monitoringeqiupment.payload.user.UserSummary;
import com.suaistuds.monitoringeqiupment.payload.user.UserUpdateRequest;
import com.suaistuds.monitoringeqiupment.service.UserService;
import com.suaistuds.monitoringeqiupment.security.CurrentUser;
import com.suaistuds.monitoringeqiupment.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping("/me")
    public UserSummary getCurrent(@CurrentUser UserPrincipal currentUser) {
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsername(@RequestParam String username) {
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmail(@RequestParam String email) {
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/{username}")
    public UserProfile getProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }

    @PutMapping("/{username}")
    public UserSummary update(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateRequest req,
            @CurrentUser UserPrincipal currentUser) {
        return userService.update(username, req, currentUser);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String username,
            @CurrentUser UserPrincipal currentUser) {
        userService.delete(username, currentUser);
    }

    @PutMapping("/{username}/role/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void giveAdmin(@PathVariable String username) {
        userService.giveAdmin(username);
    }

    @DeleteMapping("/{username}/role/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAdmin(@PathVariable String username) {
        userService.removeAdmin(username);
    }

    @PutMapping("/{username}/role/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void giveStudio(@PathVariable String username) {
        userService.giveStudio(username);
    }

    @DeleteMapping("/{username}/role/studio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStudio(@PathVariable String username) {
        userService.removeStudio(username);
    }
}
